package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Sentinel;
import com.vasconcellos.voxybases.util.ItemBuilder;
import com.vasconcellos.voxybases.util.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SentinelInventory extends SimpleInventory {

    private final Base base;

    public SentinelInventory(Base base) {
        super("inventory.sentinels", "BASE - Sentinelas", 9 * 3);

        this.base = base;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(16, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .displayname("§cVoltar")
                        .lore(" ", "§cClique aqui para voltar!", " ")
                        .damage((short) 8)
                        .build()
        ).defaultCallback(click -> {
            Player player = click.getPlayer();

            if(base.getClan() != null && !base.getClan().getMembers()
                    .contains(player.getName())) {
                HackInventory inventory = new HackInventory(base.getSafe());

                inventory.init();
                inventory.openInventory(player);

                return;
            }

            base.getSafe().open(player);
        }));

        int slot = 10;

        for(Sentinel sentinel : base.getSentinels().values()) {
            int index = slot;

            editor.setItem(slot++, InventoryItem.of(
                    new ItemBuilder(SkullCreator.SENTINEL.clone())
                            .displayname("§eSentinela #" + (slot - 10))
                            .lore(" ", "§7Clique aqui para acessar!", " ")
                            .build()
            ).defaultCallback(click -> {
                AmmoInventory inventory = new AmmoInventory(sentinel, index - 9);

                inventory.init();
                inventory.openInventory(click.getPlayer());
            }));
        }
    }
}