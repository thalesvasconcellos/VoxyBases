package com.vasconcellos.voxybases.cmd.base.sentinel;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Sentinel;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SentinelDeleteCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.deletarsentinela")
    public void setCommand(Context<Player> context, String id, String name) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhuma base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Sentinel sentinel = base.getSentinels().get(name.toLowerCase());

        if(sentinel == null) {
            context.sendMessage("§6• §cNenhuma sentinela com o nome '" + name + "' foi encontrado!");

            return;
        }

        base.getSentinels().remove(name.toLowerCase()).destroy();
        plugin.getBaseService().save(base);

        context.sendMessage("§6• §cSentinela removida com sucesso!");
    }
}
