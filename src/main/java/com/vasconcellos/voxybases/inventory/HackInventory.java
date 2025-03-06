package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import com.vasconcellos.voxybases.util.ItemBuilder;
import com.vasconcellos.voxybases.util.SkullCreator;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.enums.Roles;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HackInventory extends SimpleInventory {

    private final Safe safe;

    public HackInventory(Safe safe) {
        super("inventory.hack", "COFRE - " + (safe instanceof PersonalSafe ? "PESSOAL" : "BASE") + " (INVASOR)", 9 * 3);

        this.safe = safe;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();
        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);

        editor.setItem(11, InventoryItem.of(
                new ItemBuilder(SkullCreator.CHEST01.clone())
                        .displayname("§eArmazéns")
                        .lore(" ", "§aClique aqui para acessar!", " ")
                        .build()
        ).defaultCallback(click -> {
            StorageInventory inventory = new StorageInventory(safe);
            inventory.init();

            inventory.openInventory(click.getPlayer());
        }));

        editor.setItem(15, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .damage((short) 8)
                        .displayname("§cFECHAR")
                        .lore(" ", "§cClique aqui para fechar o menu!", " ")
                        .build()
        ).defaultCallback(click -> click.getPlayer().closeInventory()));

        if(safe instanceof ClanSafe && clanPlayer.hasClan()) {
            if(clanPlayer.getRole() != Roles.LEADER) return;

            ClanSafe clanSafe = (ClanSafe) safe;

            Base base = clanSafe.getBase();
            ProtectedRegion region = base.getRegion();

            Clan clan = clanPlayer.getClan();

            editor.setItem(13, InventoryItem.of(
                    new ItemBuilder(SkullCreator.KEYHOLE.clone())
                            .displayname("§cRoubar Base")
                            .lore("", "§aClique aqui para acessar!", " ")
                            .build()
            ).defaultCallback(click -> {
                if(base.getClan().getMembers().stream().filter(name -> Bukkit.getPlayerExact(name) != null)
                        .map(Bukkit::getPlayerExact).anyMatch(target -> {
                            Location location = target.getLocation();

                            return region.contains(
                                    location.getBlockX(),
                                    location.getBlockY(),
                                    location.getBlockZ()
                            );
                        })) {
                    player.sendMessage("§6• §cAinda possuem jogadores do clãn inimígo.");
                    player.closeInventory();

                    return;
                }

                if(safe.getStorages().values().stream().anyMatch(
                        inventory -> !isClear(inventory))) {
                    player.sendMessage("§6• §cOs armazéns deste cofre precisam estar limpos para roubar a base.");
                    player.closeInventory();

                    return;
                }

                if(VoxyBases.getInstance().getBaseManager().getByClan(clan) != null) {
                    player.sendMessage("§6• §cSeu clan já possui uma base.");
                    player.closeInventory();

                    return;
                }

                base.getClan().getMembers().stream().map(Bukkit::getPlayerExact)
                        .filter(target ->
                                target != null && region.contains(
                                        target.getLocation().getBlockX(),
                                        target.getLocation().getBlockY(),
                                        target.getLocation().getBlockZ()
                                )).forEach(target -> target.teleport(base.getSpawn()));

                if(base.getInvasion() != null) base.getInvasion().stop();
                if(base.getAlarm() != null) base.getAlarm().stop();
                if(base.getSafe().getHacking() != null) base.getSafe().getHacking().stop(true);

                base.setClan(clan);
                base.setupBanners();

                base.getSafe().updateHologram();

                VoxyBases.getInstance().getBaseService().save(base);

                base.close(true);

                player.sendMessage("§6• §aBase dominada pelo seu clan com sucesso!");
                player.closeInventory();
            }));
        }
    }

    private static boolean isClear(Inventory inventory) {
        for (ItemStack item : inventory.getContents())
            if (item != null && item.getType() != Material.AIR)
                return false;

        return true;
    }
}
