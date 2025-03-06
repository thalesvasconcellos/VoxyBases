package com.vasconcellos.voxybases.listener;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Hacking;
import com.vasconcellos.voxybases.object.Safe;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class HackingListener implements Listener {

    private final VoxyBases plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for(Safe safe : plugin.getSafeManager().getAll()) {
            if(safe.getHacking() == null) continue;

            Hacking hacking = safe.getHacking();

            if(hacking.getHacker().getUniqueId()
                    .equals(player.getUniqueId()))
                hacking.stop();
        }
    }
}