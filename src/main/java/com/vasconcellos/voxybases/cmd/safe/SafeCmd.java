package com.vasconcellos.voxybases.cmd.safe;

import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.Safe;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SafeCmd {

    @Command(name = "voxycofres", permission = "voxycofres.admin")
    public void safeCommand(Context<CommandSender> context) {
        for(String line : SafeValue.get(SafeValue::helpCommands))
            context.sendMessage(line);
    }

    @Command(name = "voxycofres.givecofre", usage = "voxycofres givecofre <nick>",
            description = "Dá um cofre para um player.")
    public void giveSafeCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Safe.getItem());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }

    @Command(name = "voxycofres.givehack", usage = "voxycofres givehack <nick>",
            description = "Dá um cofre para um player.")
    public void giveHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Safe.getHack());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }

    @Command(name = "voxycofres.giveantihack", usage = "voxycofres giveantihack <nick>",
            description = "Dá um cofre para um player.")
    public void giveAntiHackCommand(Context<CommandSender> context, OfflinePlayer player) {
        if(!player.isOnline()) {
            context.sendMessage("§6• §cJogador não encontrado");

            return;
        }

        player.getPlayer().getInventory().addItem(Safe.getStop());
        context.sendMessage("§6• §aItem adicionado ao inventário do jogador");
    }
}