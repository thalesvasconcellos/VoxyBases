package com.vasconcellos.voxybases.cmd.base.admin;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class CreateCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.criarbase", target = CommandTarget.PLAYER)
    public void createCmd(Context<Player> context, String regionName) {
        RegionManager regionManager = plugin.getWorldGuard().getRegionManager(context.getSender().getWorld());
        ProtectedRegion region = regionManager.getRegion(regionName);

        if (region == null) {
            context.sendMessage("§6• §cRegião não encontrada.");

            return;
        }

        if(plugin.getBaseManager().isBase(region)) {
            context.sendMessage("§6• §cEsta região já é uma base.");

            return;
        }

        Base base = new Base(context.getSender().getWorld(), region);
        File file = new File(plugin.getDataFolder(), "bases" +
                File.separator + base.getRegion().getId() + ".json");

        try {
            file.createNewFile();
        } catch (IOException ignored) {}

        base.setFile(file);

        plugin.getBaseManager().add(base);
        plugin.getBaseService().save(base);

        context.sendMessage("§6• §aBase '" + regionName + "' criada com sucesso!");
    }
}
