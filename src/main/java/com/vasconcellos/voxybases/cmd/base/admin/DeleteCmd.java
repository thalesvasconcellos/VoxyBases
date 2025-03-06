package com.vasconcellos.voxybases.cmd.base.admin;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DeleteCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases.deletarbase", target = CommandTarget.PLAYER)
    public void deleteCmd(Context<Player> context, String id) {
        Base base = plugin.getBaseManager().getById(id);

        if (base == null) {
            context.sendMessage("§6• §cBase não encontrada");

            return;
        }

        if(base.getInvasion() != null) base.getInvasion().stop();
        if(base.getAlarm() != null) base.getAlarm().stop();

        if(base.getSafe() != null) {
            ClanSafe safe = base.getSafe();

            safe.getHologram().delete();
            if(safe.getHacking() != null) safe.getHacking().stop();

            if(safe.getFile().delete()) plugin.getSafeManager().remove(safe);
        }

        base.setClan(null);
        base.setupBanners();

        if(base.getFile().delete()) plugin.getBaseManager().remove(base);

        context.sendMessage("§6• §aBase '" + id + "' deletada com sucesso!");
    }
}
