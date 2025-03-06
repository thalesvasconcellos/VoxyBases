package com.vasconcellos.voxybases.listener;

import com.henryfabio.minecraft.inventoryapi.controller.ViewerController;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.vasconcellos.voxybases.config.BaseValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if((event.getSlot() == -999) || ((event.getClick() == ClickType.DROP || event.getClick()
                == ClickType.CONTROL_DROP) || event.getClick() == ClickType.CREATIVE)) {
            if(event.getInventory().getName().startsWith("Armazém")) event.setCancelled(true);

            ViewerController controller = InventoryManager.getViewerController();

            controller.findViewer(player.getName()).ifPresent(viewer -> {
                if(!BaseValue.get(BaseValue::dropAndPickup))
                    event.setCancelled(true);
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if(player.isDead()) return;
        if(inventory == null) return;
        if(inventory.getName().startsWith("Armazém")) event.setCancelled(true);

        ViewerController controller = InventoryManager.getViewerController();
        controller.findViewer(player.getName()).ifPresent(viewer -> {
            if(!BaseValue.get(BaseValue::dropAndPickup))
                event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if(inventory == null) return;
        if(inventory.getName().startsWith("Armazém")) event.setCancelled(true);

        ViewerController controller = InventoryManager.getViewerController();
        controller.findViewer(player.getName()).ifPresent(viewer -> {
            if(!BaseValue.get(BaseValue::dropAndPickup))
                    event.setCancelled(true);
        });
    }
}