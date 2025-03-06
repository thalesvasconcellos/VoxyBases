package com.vasconcellos.voxybases.cmd.base.gate;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Gate;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GateDeleteCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.deletarportao")
    public void setCommand(Context<Player> context, String id, String name) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Gate gate = base.getGates().get(name.toLowerCase());

        if(gate == null) {
            context.sendMessage("§6• §cNenhum portão com o nome '" + name + "' foi encontrado!");

            return;
        }

        base.getGates().remove(name.toLowerCase());
        plugin.getBaseService().save(base);

        context.sendMessage("§6• acPortão removido com sucesso!");
    }
}
