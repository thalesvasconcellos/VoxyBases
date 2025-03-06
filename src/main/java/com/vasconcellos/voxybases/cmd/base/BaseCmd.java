package com.vasconcellos.voxybases.cmd.base;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class BaseCmd {

    private final VoxyBases plugin;

    @Command(name = "voxybases", permission = "voxybases.admin")
    public void baseCommand(Context<CommandSender> context) {
        for(String line : BaseValue.get(BaseValue::helpCommands))
            context.sendMessage(line);
    }

    @Command(name = "voxybases.givehack", usage = "voxybases givehack <nick>",
            description = "Dá um hack para um player.")
    public void giveHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Base.getHack());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }

    @Command(name = "voxybases.giveantihack", usage = "voxybases giveantihack <nick>",
            description = "Dá um antihack para um player.")
    public void giveAntiHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Base.getStop());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }

    @Command(name = "voxybases.giveportaohack", usage = "voxybases giveportaohack <nick>",
            description = "Dá um hack para um player.")
    public void giveGateHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Base.getGateHack());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }

    @Command(name = "voxybases.giveportaoantihack", usage = "voxybases giveportaoantihack <nick>",
            description = "Dá um antihack para um player.")
    public void giveGateAntiHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Base.getGateStop());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }
}