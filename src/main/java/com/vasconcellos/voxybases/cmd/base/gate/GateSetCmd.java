package com.vasconcellos.voxybases.cmd.base.gate;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Gate;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GateSetCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.setarportao")
    public void setCommand(Context<Player> context, String id, String name) {
        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        Player player = context.getSender();
        Selection selection = plugin.getWorldEdit().getSelection(player);

        if(selection == null) {
            context.sendMessage("§6• §cVocê precisa selecionar as extremidades do portão.");

            return;
        }

        Location min = selection.getMinimumPoint();
        Location max = selection.getMaximumPoint();

        if(min == null || max == null) {
            context.sendMessage("§6• §cVocê precisa selecionar as extremidades do portão.");

            return;
        }

        Gate gate = new Gate(min, max);

        if(base.getClan() == null) gate.open(false, null);

        base.getGates().put(name.toLowerCase(), gate);
        plugin.getBaseService().save(base);

        context.sendMessage("§6• §aPortão criado com sucesso!");
    }
}