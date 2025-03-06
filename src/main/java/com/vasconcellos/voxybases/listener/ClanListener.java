package com.vasconcellos.voxybases.listener;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import lombok.RequiredArgsConstructor;
import net.voxymc.clans.event.ClanDisbandEvent;
import net.voxymc.clans.model.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class ClanListener implements Listener {

    private final VoxyBases plugin;

    public void onDisband(ClanDisbandEvent event) {
        Clan clan = event.getClan();
        Base base = plugin.getBaseManager().getByClan(clan);

        if(base == null) return;

        ClanSafe safe = base.getSafe();
        Location location = safe.getLocation();

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
                .filter(target -> target != null && base.getRegion().contains(
                        target.getLocation().getBlockX(),
                        target.getLocation().getBlockY(),
                        target.getLocation().getBlockZ()
                )).forEach(target -> target.teleport(base.getSpawn()));

        base.setClan(null);
        base.setLastInvasion(null);
        base.setupBanners();

        base.getSafe().updateHologram();

        VoxyBases.getInstance().getBaseService().save(base);
    }
}