package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.simple.SimpleViewer;
import com.vasconcellos.voxybases.object.Sentinel;
import com.vasconcellos.voxybases.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AmmoInventory extends SimpleInventory {

    public static ItemStack AMMO = new ItemBuilder(Material.NETHER_STAR).build();

    @Getter private final Sentinel sentinel;

    public AmmoInventory(Sentinel sentinel, int index) {
        super("inventory.ammo", "§7Sentinela #" + index + " - Munição", 9 * 3);

        this.sentinel = sentinel;
    }

    @Override
    protected void configureViewer(SimpleViewer viewer) {
        viewer.getPropertyMap().set("sentinel", sentinel);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        for (int i = 0; i < 3 * 9; i++) {
            if (i <= 8 || i >= 3 * 9 - 9 || i == 9 || i == 17)
                editor.setItem(i, InventoryItem.of(
                        new ItemBuilder(Material.STAINED_GLASS_PANE)
                                .displayname(" ")
                                .durability((short) 7)
                                .build()
                ));
        }

        int slot = 10;
        int ammo = sentinel.getAmmo();

        int quotient = ammo / 64;
        int remainder = ammo % 64;

        for(int i = 0; i < quotient; i++) {
            editor.setItem(slot++, InventoryItem.of(
                    new ItemBuilder(AMMO.clone())
                            .amount(64)
                            .build()
            ));
        }

        if(remainder != 0)
            editor.setItem(slot, InventoryItem.of(
                    new ItemBuilder(AMMO.clone())
                            .amount(remainder)
                            .build()));
    }
}