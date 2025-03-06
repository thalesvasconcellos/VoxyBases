package com.vasconcellos.voxybases.listener;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class BannerListener implements Listener {

    public static Map<UUID, Base> SELECTION = new HashMap<>();

    private final VoxyBases plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(!SELECTION.containsKey(player.getUniqueId())) return;

        Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        Base base = SELECTION.remove(player.getUniqueId());

        if(!block.getType().name().endsWith("_BANNER")) {
            player.sendMessage("§c• §cEste bloco não é um banner!");

            return;
        }

        base.getBanners().add(block.getLocation());
        base.setupBanners();

        plugin.getBaseService().save(base);

        player.sendMessage("§c• §aVocê setou o banner com sucesso!");
    }
}
