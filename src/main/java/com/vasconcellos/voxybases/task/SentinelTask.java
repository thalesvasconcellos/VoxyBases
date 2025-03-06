package com.vasconcellos.voxybases.task;

import com.henryfabio.minecraft.inventoryapi.controller.ViewerController;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.inventory.AmmoInventory;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Sentinel;
import com.vasconcellos.voxybases.util.InvisibleSnowball;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SentinelTask implements Runnable {

    private final VoxyBases plugin;

    @Override
    public void run() {
        for(Base base : plugin.getBaseManager().getBases().values()) {
            if(base.getClan() == null) continue;

            for(Sentinel sentinel : base.getSentinels().values()) {
                if(sentinel.getAmmo() <= 0) continue;

                ArmorStand stand = sentinel.getStand();
                Location location = stand.getLocation();

                double range = BaseValue.get(BaseValue::sentinelRange);

                List<Player> entities = stand.getWorld().getNearbyEntities(location, range, range, range)
                        .stream().filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .filter(player -> !player.hasMetadata("NPC"))
                        .filter(player -> player.getGameMode() == GameMode.SURVIVAL && !player.hasPotionEffect
                                (PotionEffectType.INVISIBILITY) && !player.isDead())
                        .filter(player -> !(base.isAlly(player) || base.isTrusted(player)))
                        .collect(Collectors.toList());

                if(entities.size() == 0) continue;

                Player closest = entities.get(0);
                double closestDist = closest.getLocation().distance(location);

                for (Player entity : entities)
                    if (entity.getLocation().distance(location) < closestDist) {
                        closestDist = entity.getLocation().distance(location);
                        closest = entity;
                    }

                Vector vector = location.toVector().subtract(closest.getLocation().toVector());

                Location to = stand.getEyeLocation().setDirection(vector);
                to.setX(location.getX()); to.setY(location.getY()); to.setZ(location.getZ());

                stand.teleport(to);

                if(System.currentTimeMillis() >= sentinel.getLastShot() + BaseValue.get(BaseValue::sentinelDelay)) {
                    AtomicBoolean cancel = new AtomicBoolean();

                    base.getClan().getMembers().stream().map(Bukkit::getPlayerExact)
                            .filter(Objects::nonNull).forEach(player -> {
                                ViewerController controller = InventoryManager.getViewerController();

                                controller.findViewer(player.getName()).ifPresent(viewer -> {
                                    if(viewer.getCustomInventory() instanceof AmmoInventory) cancel.set(true);
                                });
                            });

                    if(cancel.get()) continue;

                    Vector p1 = closest.getLocation().toVector();
                    p1.setY(p1.getY() + 1.25D);
                    Vector p2 = stand.getLocation().toVector();

                    Location normalized = location.clone().add(p1.clone().subtract(p2).normalize())
                            .add(0, BaseValue.get(BaseValue::sentinelShootHeight), 0);
                    Vector velocity = p1.clone().subtract(p2).normalize();

                    new InvisibleSnowball(normalized, velocity).sucess(() -> {
                        sentinel.setAmmo(sentinel.getAmmo() - 1);

                        for(int i = 0; i < 20; i++)
                            new ParticleBuilder(ParticleEffect.SMOKE_NORMAL, normalized.clone()
                                    .add(randomFloat(), randomFloat(), randomFloat()))
                                    .display();

                        Snowball snowball = (Snowball) location.getWorld().spawnEntity(normalized, EntityType.SNOWBALL);

                        snowball.setMetadata("sentinel", new FixedMetadataValue(plugin,
                                BaseValue.get(BaseValue::sentinelDamage)));
                        snowball.setVelocity(velocity.multiply(BaseValue.get(BaseValue::sentinelSpeed)));
                    }).spawn();

                    sentinel.setLastShot(System.currentTimeMillis());
                }
            }
        }
    }

    private double randomFloat() {
        Random random = new Random();
        int number = random.nextInt(((int) (0.5 / 0.01)) + 1);

        return number * 0.01;
    }
}
