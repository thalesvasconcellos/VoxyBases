package com.vasconcellos.voxybases.listener;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.object.Alarm;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Invasion;
import lombok.RequiredArgsConstructor;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


@RequiredArgsConstructor
public class RegionListener implements Listener {

   private final VoxyBases plugin;

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player player = event.getPlayer();
        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);

        ProtectedRegion region = event.getRegion();
        Base base = plugin.getBaseManager().getByRegion(region);

        if(base == null) return;

        if(!clanPlayer.hasClan()) {
            player.sendMessage("§cVocê precisa de um clan para entrar nesta base.");
            player.teleport(base.getSpawn());

            return;
        }

        Clan clan = clanPlayer.getClan();

        if(base.getClan() == null) return;

        Invasion invasion = base.getInvasion();

        if(base.getClan() != null && (base.getClan().equals(clan) || base.isAlly(clan))) return;

        Base invading = plugin.getBaseManager().getBases().values().stream().filter(toMatch ->
                toMatch.getInvasion() != null && toMatch.getInvasion().getClan().equals(clan)
        ).findAny().orElse(null);

        if(invading != null && !invading.equals(base)) {
            player.sendMessage(BaseValue.get(BaseValue::gateAlreadyInvadingMessage));
            player.teleport(base.getSpawn());

            return;
        }

        if(BaseValue.get(BaseValue::needToHack) && (invasion == null || invasion.
                getStatus() != Invasion.InvasionStatus.COMPLETED)) {
            player.sendMessage("§cVocê precisa hackear esta base para poder entrar.");
            player.teleport(base.getSpawn());

            return;
        }

        if(!BaseValue.get(BaseValue::needToHack)) {
            if(invasion != null && !invasion.getClan().equals(clan)) {
                player.sendMessage("§cUma invasão está acontecendo nesta base.");
                player.teleport(base.getSpawn());

                return;
            }

            if(base.getAlarm() != null) return;

            Alarm alarm = new Alarm(base, clan);
            alarm.start();

            base.setAlarm(alarm);

            Invasion newInvasion = new Invasion(base, clan);
            newInvasion.setStatus(Invasion.InvasionStatus.COMPLETED);

            newInvasion.start();

            base.setInvasion(newInvasion);

            return;
        }

        if(!invasion.getClan().equals(clan)) {
            player.sendMessage("§cUma invasão está acontecendo nesta base.");
            player.teleport(base.getSpawn());

            return;
        }

        if(base.getAlarm() != null) return;

        Alarm alarm = new Alarm(base, clan);
        alarm.start();

        base.setAlarm(alarm);
    }
}