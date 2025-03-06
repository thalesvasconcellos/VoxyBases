package com.vasconcellos.voxybases.cmd.base.banner;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.listener.BannerListener;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BannerSetCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.setarbanner")
    public void setCommand(Context<Player> context, String id) {

        Base base = plugin.getBaseManager().getById(id);

        if(base == null) {
            context.sendMessage("§6• §cNenhum base com o nome '" + id + "' foi encontrada!");

            return;
        }

        if(BannerListener.SELECTION.containsKey(context.getSender().getUniqueId())) {
            context.sendMessage("§6• §aClique no banner que você deseja setar como o da base!");
            return;
        }

        BannerListener.SELECTION.put(context.getSender().getUniqueId(), base);
        context.sendMessage("§6• §aClique no banner que você deseja setar como o da base!");
    }
}