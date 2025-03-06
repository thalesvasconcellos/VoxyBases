package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Invasion;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.SafeState;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

public class BaseInventory extends SimpleInventory {

    public BaseInventory() {
        super("inventory.base", "§7BASE - INFORMAÇÕES", 9 * 3);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();

        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);
        Clan clan = clanPlayer.getClan();

        Base base = VoxyBases.getInstance().getBaseManager().getByClan(clan);
        ProtectedRegion region = base.getRegion();

        Invasion invasion = base.getLastInvasion();

        Safe safe = base.getSafe();
        Location location = safe.getLocation();

        editor.setItem(10, InventoryItem.of(
                new ItemBuilder(Material.IRON_FENCE)
                        .displayname("§aAbrir PORTÃO")
                        .lore(" ", "§7Tempo para fechar: §c15 segundos", " ",
                                "§aClique aqui para abrir o portão!" , " ")
                        .build()
        ).defaultCallback(click -> {
            if(base.getInvasion() != null || base.getSafe() != null && (base.getSafe().getState() ==
                    SafeState.HACKING || base.getSafe().getState() == SafeState.HACKED)) {
                player.closeInventory();
                player.sendMessage("§6• §cVocê não pode interagir com o " +
                        "portão pois sua base está sendo atacada.");

                return;
            }

            if(base.isOpen()) {
                player.closeInventory();
                player.sendMessage("§6• §cO portão já se encontra aberto!");

                return;
            }

            base.open(true, () -> new BukkitRunnable() {
                @Override
                public void run() {
                    if(base.getClan() != null)
                        base.close(true);
                }
            }.runTaskLater(VoxyBases.getInstance(), 20L * 15));

            player.sendMessage("§6• §aPortão aberto com sucesso!");
            player.closeInventory();
        }));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        editor.setItem(11, InventoryItem.of(
                new ItemBuilder(SkullCreator.MONITOR.clone())
                        .displayname("§cUltima INVASÃO")
                        .lore(invasion == null ?
                                Arrays.asList(
                                        " ",
                                        "§7Clan: §cNenhum",
                                        "§7Jogador: §cNenhum",
                                        "§7Data: §cNenhuma",
                                        " "
                                ) : Arrays.asList(" ", "§7Clan: " + invasion.getClan()
                                .getColoredTag() + " " + invasion.getClan().getNameClan(),
                                "§7Data: " + invasion.getDate().format(formatter), " "
                        )).build()
        ));

        editor.setItem(12, InventoryItem.of(
                new ItemBuilder(SkullCreator.TOY_HOUSE.clone())
                        .displayname("§eLocalização da BASE")
                        .lore(" ", "§7X: §e" + location.getBlockX() +
                                " §7Y: §e" + location.getBlockY() +
                                " §7Z: §e" + location.getBlockZ(), " ",
                                "§aClique para exibir a",
                                "§alocalização no chat!",
                                " ")
                        .build()
        ).defaultCallback(click -> {
            player.closeInventory();
            player.sendMessage("§7X: §e" + location.getBlockX() +
                    " §7Y: §e" + location.getBlockY() +
                    " §7Z: §e" + location.getBlockZ());
        }));

        if(clanPlayer.getRole() == Roles.LEADER)
            editor.setItem(13, InventoryItem.of(
                    new ItemBuilder(Material.BARRIER)
                            .displayname("§cAbandonar Base")
                            .lore(" ", "§7Você deseja abandonar a base?",
                                    " ", "§aClique aqui para abandona-la!",
                                    " ")
                            .build()
                    ).defaultCallback(click -> {
                        if(base.getInvasion() != null || base.getSafe() != null && (base.getSafe().getState() ==
                                SafeState.HACKING || base.getSafe().getState() == SafeState.HACKED)) {
                            player.closeInventory();
                            player.sendMessage("§6• §cVocê não pode abandonar a base sobre ataque.");

                            return;
                        }

                        TreeMap<Integer, Inventory> storages = new TreeMap<>(safe.getStorages());

                        storages.subMap(2, 6).values().stream()
                                .map(Inventory::getContents)
                                .forEach(items -> Arrays.stream(items).forEach(item -> {
                                    if(item == null) return;

                                    location.getWorld().dropItemNaturally(
                                            location.clone().add(0, 1, 0),
                                            item);
                                }));

                        safe.setSecurity(1);
                        safe.setStorages(new HashMap<>(storages.subMap(0, 2)));

                        base.open(true);
                        base.getClan().getMembers().stream().map(Bukkit::getPlayerExact)
                                .filter(target -> target != null && region.contains(
                                        target.getLocation().getBlockX(),
                                        target.getLocation().getBlockY(),
                                        target.getLocation().getBlockZ()
                                )).forEach(target -> target.teleport(base.getSpawn()));

                        base.setClan(null);
                        base.setLastInvasion(null);
                        base.setupBanners();

                        base.getSafe().updateHologram();

                        VoxyBases.getInstance().getBaseService().save(base);

                        player.sendMessage("§6• §aVocê abandonou a base com sucesso!");
                        player.closeInventory();
                    }));

        editor.setItem(15, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .damage((short) 8)
                        .displayname("§cFECHAR")
                        .lore(" ", "§cClique aqui para fechar o menu!", " ")
                        .build()
        ).defaultCallback(click -> player.closeInventory()));
    }
}