package com.vasconcellos.voxybases.listener;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Invasion;
import com.vasconcellos.voxybases.util.ActionBar;
import lombok.RequiredArgsConstructor;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@RequiredArgsConstructor
public class GateListener implements Listener {

    private final VoxyBases plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);

        if(!clanPlayer.hasClan()) return;

        Clan clan = clanPlayer.getClan();

        ItemStack item = event.getItem();

        Block block = event.getClickedBlock();
        plugin.getBaseManager().getByBlock(block, (base, gate) -> { event.setCancelled(true);
            Invasion invasion = base.getInvasion();

            if(base.isOpen() || base.getClan() == null) return;

            if(base.getClan().equals(clan) || base.isAlly(clan)) {
                if(invasion == null) return;

                if(invasion.getStatus() != Invasion.InvasionStatus.OCURRING) {
                    player.sendMessage("§6• §cEste portão já foi hackeado.");

                    return;
                }

                if(item == null || !item.isSimilar(Base.getGateStop())) {
                    player.sendMessage("§6• §cÉ necessário de um computador parar o hack deste portão.");

                    return;
                }

                if (item.getAmount() == 1)
                    player.setItemInHand(null);
                else item.setAmount(item.getAmount() - 1);

                invasion.stop();

                base.setInvasion(null);
                base.setLastInvasion(invasion);

                invasion.getClan().getMembers().stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .forEach(target -> ActionBar.send(target,
                                "§aO hack do portão foi parado!"));

                base.getClan().getMembers().stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .forEach(target -> ActionBar.send(target,
                                "§aO hack do portão foi parado!"));

                player.sendMessage("§6• §aVocê parou o hack do portão com sucesso!");
            } else {
                if(invasion != null && invasion.getClan().equals(clan)) {
                    if(invasion.getStatus() == Invasion.InvasionStatus.OCURRING)
                        player.sendMessage("§6• §cHack em andamento, aguarde para ter acesso a base.");

                    return;
                }

                Base invading = plugin.getBaseManager().getBases().values().stream().filter(
                        toMatch -> toMatch.getInvasion() != null &&
                                toMatch.getInvasion().getClan().equals(clan)
                ).findAny().orElse(null);

                if(invading != null && !invading.equals(base)) {
                    player.sendMessage(BaseValue.get(BaseValue::gateAlreadyInvadingMessage));

                    return;
                }

                if(BaseValue.get(BaseValue::gateInvadeWithoutOnlinePlayers) && base.getClan().getMembers().stream()
                        .map(Bukkit::getPlayerExact).noneMatch(Objects::nonNull)) {
                    player.sendMessage(BaseValue.get(BaseValue::gateCantInvadeMessage));

                    return;
                }

                if(item == null || !item.isSimilar(Base.getGateHack())) {
                    player.sendMessage("§6• §cÉ necessário de um computador para hackear este portão.");

                    return;
                }

                if (item.getAmount() == 1)
                    player.setItemInHand(null);
                else item.setAmount(item.getAmount() - 1);

                Invasion newInvasion = new Invasion(base, clan);
                newInvasion.start();

                base.setInvasion(newInvasion);

                clan.getMembers().stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .forEach(target -> ActionBar.send(target,
                                "§aSeu clan está invadindo a base de " +
                                        "" + base.getClan().getNameClan()));

                base.getClan().getMembers().stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .forEach(target -> ActionBar.send(target,
                                "§aSeu clan está sendo invadindo por " +
                                        "" + clan.getNameClan()));

                for(String name : clan.getMembers())

                player.sendMessage("§6• §aHack iniciado, aguarde para ter acesso a base.");
            }
        });
    }
}