package com.vasconcellos.voxybases.listener;

import com.henryfabio.minecraft.inventoryapi.controller.ViewerController;
import com.henryfabio.minecraft.inventoryapi.event.impl.CustomInventoryClickEvent;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.vasconcellos.voxybases.inventory.AmmoInventory;
import com.vasconcellos.voxybases.util.InvisibleSnowball;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Map;

public class SentinelListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if(!damager.hasMetadata("sentinel")) {
            InvisibleSnowball snowball = InvisibleSnowball.SNOWBALLS.entrySet().stream().filter(entry ->
                    damager.hasMetadata(entry.getKey().toString()))
                    .map(Map.Entry::getValue).findAny().orElse(null);

            if(snowball == null) return;

            Runnable runnable = snowball.getRunnable();
            if(runnable != null) runnable.run();

            event.setDamage(0);
            event.setCancelled(true);

            return;
        }

        List<MetadataValue> damage = damager.getMetadata("sentinel");
        MetadataValue value = damage.get(0);

        try {
            ((CraftPlayer) event.getEntity()).getHandle().damageEntity(DamageSource.GENERIC, value.asInt());
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onCustomInventoryClick(CustomInventoryClickEvent event) {
        if(!(event.getCustomInventory() instanceof AmmoInventory)) return;
        if(event.getItemStack() == null || event.getItemStack().getType() == Material.AIR ||
                event.getItemStack().isSimilar(AmmoInventory.AMMO))
            event.setCancelled(false);
    }

    @EventHandler
    public void oninventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        ViewerController controller = InventoryManager.getViewerController();

        controller.findViewer(player.getName()).ifPresent(viewer -> {
            if(viewer.getCustomInventory() instanceof AmmoInventory) {
                AmmoInventory inventory = viewer.getCustomInventory();

                int ammo = 0;

                for(int i = 10; i <= 16; i++) {
                    ItemStack item = event.getInventory().getItem(i);

                    if(item == null || !item.isSimilar(AmmoInventory.AMMO)) continue;

                    ammo += item.getAmount();
                }

                inventory.getSentinel().setAmmo(ammo);
            }
        });
    }
}