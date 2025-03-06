package com.vasconcellos.voxybases.cmd.base.spawn;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SpawnSetCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.setarspawn")
    public void setCommand(Context<Player> context, String id) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Player player = context.getSender();
        Location location = player.getLocation();

        base.setSpawn(location);
        plugin.getBaseService().save(base);

        player.sendMessage("§6• §aSpawn setado com sucesso!");
    }


    @Command(name = "voxybases.tpspawn")
    public void tpCommand(Context<Player> context, String id) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Player player = context.getSender();
        Location spawn = base.getSpawn();

        if (spawn == null) {
            context.sendMessage("§6• §cEsta base não possui spawn setado");

            return;
        }

        player.teleport(spawn);
        player.sendMessage("§6• §aVocê foi teleportado com sucesso!");
    }
}