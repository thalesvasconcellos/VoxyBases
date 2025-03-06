package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.util.ItemBuilder;
import com.vasconcellos.voxybases.util.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ClanSafeInventory extends SimpleInventory {

    private final ClanSafe safe;

    public ClanSafeInventory(ClanSafe safe, Player player) {
        super("inventory.clan.inventory", "§7COFRE - BASE" + (safe.isLeader(player.getName())
                ? "" : " (MEMBRO)"), 9 * 3);

        this.safe = safe;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();
        boolean owner = safe.isLeader(player.getName());

        editor.setItem(11, InventoryItem.of(
                new ItemBuilder(SkullCreator.CHEST01.clone())
                        .displayname("§eArmazéns")
                        .lore(" ", "§aClique aqui para acessar!", " ")
                        .build()
        ).defaultCallback(click -> {
            StorageInventory inventory = new StorageInventory(safe);

            inventory.init();
            inventory.openInventory(player);
        }));

        if(owner)
            editor.setItem(12, InventoryItem.of(
                    new ItemBuilder(SkullCreator.ALARM_ON.clone())
                            .displayname("§cSistema de Segurança")
                            .lore(" ", "§aClique aqui para acessar!", " ")
                            .build()
            ).defaultCallback(click -> {
                SecurityInventory inventory = new SecurityInventory(safe);

                inventory.init();
                inventory.openInventory(player);
            }));

        editor.setItem(15, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .damage((short) 8)
                        .displayname("§cFECHAR")
                        .lore(" ", "§cClique aqui para fechar o menu!", " ")
                        .build()
        ).defaultCallback(click -> click.getPlayer().closeInventory()));
    }
}
