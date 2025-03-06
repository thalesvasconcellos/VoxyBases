package com.vasconcellos.voxybases.config.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.config.SafeValue;

public class ConfigRegistry {

    public static void enable(VoxyBases plugin) {
        BukkitConfigurationInjector injector = new BukkitConfigurationInjector(plugin);

        injector.saveDefaultConfiguration(plugin,
                "base.yml",
                "safe.yml"
        );

        injector.injectConfiguration(SafeValue.instance(), BaseValue.instance());
    }
}