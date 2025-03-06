package com.vasconcellos.voxybases;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.vasconcellos.voxybases.cmd.registry.CommandRegistry;
import com.vasconcellos.voxybases.config.registry.ConfigRegistry;
import com.vasconcellos.voxybases.listener.registry.ListenerRegistry;
import com.vasconcellos.voxybases.manager.BaseManager;
import com.vasconcellos.voxybases.manager.SafeManager;
import com.vasconcellos.voxybases.service.BaseService;
import com.vasconcellos.voxybases.service.SafeService;
import com.vasconcellos.voxybases.task.CleanerTask;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
public class VoxyBases extends JavaPlugin {

    private Economy economy;
    private WorldGuardPlugin worldGuard;
    private WorldEditPlugin worldEdit;

    private BaseService baseService;
    private BaseManager baseManager;

    private SafeService safeService;
    private SafeManager safeManager;

    @SneakyThrows
    @Override
    public void onEnable() {
        if(!setupEconomy()) {
            getLogger().severe("Vault não encontrado. O plugin foi desabilitado!");
            getPluginLoader().disablePlugin(this);

            return;
        }

        if(!setupWorldGuard()) {
            getLogger().severe("WorldGuard não encontrado. O plugin foi desabilitado!");
            getPluginLoader().disablePlugin(this);

            return;
        }

        if(!setupWorldEdit()) {
            getLogger().severe("WorldEdit não encontrado. O plugin foi desabilitado!");
            getPluginLoader().disablePlugin(this);

            return;
        }

        if(getServer().getPluginManager().getPlugin("VoxyBases") == null) {
            getLogger().severe("VoxyBases não encontrado. O plugin foi desabilitado!");
            getPluginLoader().disablePlugin(this);

            return;
        }

        ConfigRegistry.enable(this);
        CommandRegistry.enable(this);
        ListenerRegistry.enable(this);
        InventoryManager.enable(this);

        safeService = new SafeService();

        safeManager = new SafeManager();
        safeManager.setSafes(safeService.findAll());

        baseService = new BaseService();

        baseManager = new BaseManager();
        baseManager.setBases(baseService.findAll());

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new CleanerTask
                (this), 6000L, 6000L);

        baseManager.getBases().values().forEach(base -> {
            if(base.getClan() == null) base.open(false);
        });
    }

    @Override
    public void onDisable() {
        safeManager.getAll().forEach(safe -> safeService.save(safe));
        baseManager.getBases().values().forEach(base -> {
            base.close(false);
            baseService.save(base);
        });
    }

    private boolean setupWorldGuard() {
        if(getServer().getPluginManager().getPlugin("WorldGuard") == null)
            return false;

        worldGuard = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");

        return true;
    }

    private boolean setupWorldEdit() {
        if(getServer().getPluginManager().getPlugin("WorldEdit") == null)
            return false;

        worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        return true;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null)
            return false;

        economy = rsp.getProvider();
        return economy != null;
    }

    public static VoxyBases getInstance() {
        return getPlugin(VoxyBases.class);
    }
}