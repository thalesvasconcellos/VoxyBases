package com.vasconcellos.voxybases.listener;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.function.Consumer;

public class HologramListener implements Listener {

    public static final Map<String, Consumer<Player>> CONSUMERS = Maps.newHashMap();

    @EventHandler
    public void onHologramClick(HologramClickEvent event) {
        Consumer<Player> consumer = CONSUMERS.get(event.getHologram().getName());

        if(consumer != null) consumer.accept(event.getPlayer());
    }
}