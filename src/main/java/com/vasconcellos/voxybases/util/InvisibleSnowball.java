package com.vasconcellos.voxybases.util;

import com.google.common.collect.Maps;
import com.vasconcellos.voxybases.VoxyBases;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class InvisibleSnowball {

    public static final Map<UUID, InvisibleSnowball> SNOWBALLS = Maps.newConcurrentMap();

    private final UUID id = UUID.randomUUID();

    private final Location location;
    private final Vector velocity;

    @Getter private Runnable runnable;

    public InvisibleSnowball(Location location, Vector velocity) {
        this.location = location;
        this.velocity = velocity.clone();
    }

    public void spawn() { SNOWBALLS.put(id, this);
        Snowball snowball = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);

        for(Player player : Bukkit.getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(snowball.getEntityId());
            ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }

        snowball.setMetadata(id.toString(), new FixedMetadataValue(VoxyBases.getInstance(), this));
        snowball.setVelocity(velocity.multiply(100));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SNOWBALLS.remove(id);
                snowball.remove();
            }
        }, 500L);
    }

    public InvisibleSnowball sucess(Runnable runnable) {
        this.runnable = runnable;

        return this;
    }
}
