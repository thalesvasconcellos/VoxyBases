package com.vasconcellos.voxybases.task;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CleanerTask implements Runnable {

    private final VoxyBases plugin;

    @Override
    public void run() {
        if(!SafeValue.get(SafeValue::inactivePlayer)) return;

        for(Safe safe : new ArrayList<>(plugin.getSafeManager().getAll())) {
            if(!(safe instanceof PersonalSafe)) continue;

            PersonalSafe personalSafe = (PersonalSafe) safe;
            UUID owner = personalSafe.getOwner();

            long lastPlayed = Bukkit.getPlayer(owner) == null ? Bukkit.getOfflinePlayer(owner)
                    .getLastPlayed() : System.currentTimeMillis();
            long diff = TimeUnit.MILLISECONDS.toDays(System
                    .currentTimeMillis() - lastPlayed);

            if(diff >= SafeValue.get(SafeValue::inactiveDays)) {
                safe.getLocation().getBlock().setType(Material.AIR);
                safe.getHologram().delete();

                plugin.getSafeService().delete(safe);
                plugin.getSafeManager().remove(safe);
            }
        }
    }
}