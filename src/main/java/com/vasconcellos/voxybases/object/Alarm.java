package com.vasconcellos.voxybases.object;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.util.ActionBar;
import lombok.Getter;
import net.voxymc.clans.model.Clan;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

@Getter
public class Alarm {

    private final Base base;
    private final Clan clan, invasor;

    private BukkitTask task;

    public Alarm(Base base, Clan invasor) {
        this.base = base;

        this.clan = base.getClan();
        this.invasor = invasor;
    }

    public void start() {
        clan.getMembers().stream()
                .map(Bukkit::getPlayerExact)
                .filter(Objects::nonNull)
                .forEach(target -> target.sendTitle(
                        BaseValue.get(BaseValue::alarmTitleClan).get(0)
                                .replace("{clan_colored_tag}", invasor.getColoredTag()),
                        BaseValue.get(BaseValue::alarmTitleClan).get(1)
                                .replace("{clan_colored_tag}", invasor.getColoredTag())));

        invasor.getMembers().stream()
                .map(Bukkit::getPlayerExact)
                .filter(Objects::nonNull)
                .forEach(target -> target.sendTitle(
                        BaseValue.get(BaseValue::alarmTitleAttacking).get(0)
                                .replace("{clan_colored_tag}", clan.getColoredTag()),
                        BaseValue.get(BaseValue::alarmTitleAttacking).get(1)
                                .replace("{clan_colored_tag}", clan.getColoredTag())));

        task = new BukkitRunnable() {
             boolean first = true;

            @Override
            public void run() {
                if(first) {
                    clan.getMembers().stream()
                            .map(Bukkit::getPlayerExact)
                            .filter(Objects::nonNull)
                            .forEach(target -> ActionBar.send(
                                    target,
                                    BaseValue.get(BaseValue::alarmActionClan01)
                                            .replace("{clan_colored_tag}",
                                                    invasor.getColoredTag())
                            ));

                    invasor.getMembers().stream()
                            .map(Bukkit::getPlayerExact)
                            .filter(Objects::nonNull)
                            .forEach(target -> ActionBar.send(
                                    target,
                                    BaseValue.get(BaseValue::alarmActionAttacking01)
                                            .replace("{clan_colored_tag}",
                                                    clan.getColoredTag())
                            ));
                } else {
                    clan.getMembers().stream()
                            .map(Bukkit::getPlayerExact)
                            .filter(Objects::nonNull)
                            .forEach(target -> ActionBar.send(
                                    target,
                                    BaseValue.get(BaseValue::alarmActionClan02)
                                            .replace("{clan_colored_tag}",
                                                    invasor.getColoredTag())
                            ));

                    invasor.getMembers().stream()
                            .map(Bukkit::getPlayerExact)
                            .filter(Objects::nonNull)
                            .forEach(target -> ActionBar.send(
                                    target,
                                    BaseValue.get(BaseValue::alarmActionAttacking02)
                                            .replace("{clan_colored_tag}",
                                                    clan.getColoredTag())
                            ));
                }

                first = !first;
            }
        }.runTaskTimer(VoxyBases.getInstance(), 0L, BaseValue.get(BaseValue::alarmActionDelay));
    }

    public void stop() {
        if(task != null) task.cancel();

        base.setAlarm(null);
    }
}