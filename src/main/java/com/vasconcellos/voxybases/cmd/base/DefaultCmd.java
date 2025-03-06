package com.vasconcellos.voxybases.cmd.base;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.inventory.BaseInventory;
import com.vasconcellos.voxybases.object.Base;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DefaultCmd {

    private final VoxyBases plugin;

    @Command(name = "base")
    public void defaultCommand(Context<Player> context) {
        Player player = context.getSender();
        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);

        if(clanPlayer == null || !clanPlayer.hasClan()) {
            player.sendMessage("§6• §cVocê não está um clan!");

            return;
        }

        Clan clan = clanPlayer.getClan();
        Base base = plugin.getBaseManager().getByClan(clan);

        if(base == null) {
            player.sendMessage("§6• §cSeu clan não possui uma base!");

            return;
        }

        BaseInventory inventory = new BaseInventory();
        inventory.init();

        inventory.openInventory(player);
    }
}
