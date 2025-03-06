package com.vasconcellos.voxybases.listener.registry;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.stream.Stream;

public class ListenerRegistry {

    public static void enable(VoxyBases plugin) {
        Listener[] classes = {
                new BannerListener(plugin),
                new SafeListener(plugin),
                new HackingListener(plugin),
                new GateListener(plugin),
                new RegionListener(plugin),
                new ClanListener(plugin),
                new InventoryListener()
        };

        PluginManager pluginManager = Bukkit.getPluginManager();
        Stream.of(classes).forEach(listener -> pluginManager
                .registerEvents(listener, plugin));
    }
}