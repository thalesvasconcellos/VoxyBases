package com.vasconcellos.voxybases.cmd.base.sentinel;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Sentinel;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SentinelSetCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.setarsentinela")
    public void setCommand(Context<Player> context, String id, String name) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhuma base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Player player = context.getSender();

        Sentinel sentinel = new Sentinel(player.getLocation());

        base.getSentinels().put(name.toLowerCase(), sentinel);
        plugin.getBaseService().save(base);

        context.sendMessage("§6• §aSentinela setada com sucesso!");
    }
}