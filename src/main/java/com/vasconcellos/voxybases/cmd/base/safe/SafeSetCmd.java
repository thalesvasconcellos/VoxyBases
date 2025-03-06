package com.vasconcellos.voxybases.cmd.base.safe;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class SafeSetCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.setarcofre")
    public void setCommand(Context<Player> context, String id) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        if(base.getSafe() != null) {
            context.sendMessage("§6• §cEsta base já possui um cofre.");

            return;
        }

        Player player = context.getSender();
        Location location = player.getLocation();

        location.getBlock().setType(Material.getMaterial(BaseValue.get(BaseValue::safeItem)));

        ClanSafe safe = new ClanSafe(UUID.randomUUID(), location.getBlock().getLocation());
        safe.setBase(base);

        safe.getStorages().put(1, Bukkit.createInventory(null, 9 * 6, "Armazém #1"));
        safe.updateHologram();

        File file = new File(plugin.getDataFolder(), "cofres" + File.separator + safe.getId() + ".json");

        try {
            file.createNewFile();
        } catch (IOException ignored) {}

        safe.setFile(file);

        plugin.getSafeManager().add(safe);
        plugin.getSafeService().save(safe);

        base.setSafe(safe);

        player.sendMessage("§6• §aCofre colocado com sucesso!");
    }
}