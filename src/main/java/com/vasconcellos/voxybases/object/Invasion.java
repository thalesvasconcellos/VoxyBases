package com.vasconcellos.voxybases.object;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.util.ActionBar;
import lombok.Data;
import net.voxymc.clans.model.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

@Data
public class Invasion {

    public enum InvasionStatus {
        OCURRING, COMPLETED
    }

    private Base base;

    private Clan clan;
    private String invasor;

    private InvasionStatus status = InvasionStatus.OCURRING;
    private boolean safeHacked = false;

    private LocalDateTime date = LocalDateTime.now();

    private int time = BaseValue.get(BaseValue::gateHackTime);
    private int stopTime = BaseValue.get(BaseValue::timeToStopInvasion);

    private BukkitTask task, stopTask;

    public Invasion(Base base, Clan clan) {
        this.base = base;
        this.clan = clan;
    }

    public Invasion(Base base, Player invasor) {
        this.base = base;
        this.invasor = invasor.getName();
    }

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(time <= 0 || status == InvasionStatus.COMPLETED) { cancel();
                    if(status != InvasionStatus.COMPLETED)
                        clan.getMembers().stream()
                                .map(Bukkit::getPlayerExact)
                                .filter(Objects::nonNull)
                                .forEach(target -> ActionBar.send(
                                        target,
                                        BaseValue.get(BaseValue::gateActionHacked)
                                                .replace("{tempo}",
                                                        String.valueOf(time))
                                ));

                    base.open(true); status = InvasionStatus.COMPLETED;

                    stopTask = new BukkitRunnable() {

                        @Override
                        public void run() {
                            long count = Bukkit.getOnlinePlayers().stream().filter(player -> {
                                Location location = player.getLocation();

                                return clan.getMembers().contains(player.getName()) &&
                                        base.getRegion().contains(
                                                location.getBlockX(),
                                                location.getBlockY(),
                                                location.getBlockZ()
                                        );
                            }).count();

                            if(count <= 0) { stopTime--;
                                if(base.getAlarm() != null) base.getAlarm().stop();
                            } else stopTime = BaseValue.get(BaseValue::timeToStopInvasion);

                            if(stopTime <= 0) { stop(); cancel();
                                base.setLastInvasion(Invasion.this);
                                base.setInvasion(null);

                                if(safeHacked) {
                                    Safe safe = base.getSafe();
                                    Location location = safe.getLocation().clone().add(0, 1, 0);

                                    safe.setSecurity(1);

                                    TreeMap<Integer, Inventory> storages = new TreeMap<>(safe.getStorages());

                                    storages.subMap(2, 6).values().stream()
                                            .map(Inventory::getContents)
                                            .forEach(items -> Arrays.stream(items).forEach(item -> {
                                                if(item == null) return;

                                                location.getWorld().dropItemNaturally(
                                                        location,
                                                        item);
                                            }
                                            ));

                                    safe.setStorages(new HashMap<>(storages.subMap(0, 2)));

                                    Hacking hacking = safe.getHacking();
                                    if(hacking != null) hacking.stop();
                                }

                                base.close(true);
                            }
                        }
                    }.runTaskTimer(VoxyBases.getInstance(), 0L, 20L);

                    return;
                }

                clan.getMembers().stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .forEach(target -> ActionBar.send(
                                target,
                                BaseValue.get(BaseValue::gateActionHacking)
                                        .replace("{tempo}",
                                                String.valueOf(time))
                        ));

                time--;
            }
        }.runTaskTimer(VoxyBases.getInstance(), 20L, 20L);
    }

    public void stop() {
        if(task != null) task.cancel();
        if(stopTask != null) stopTask.cancel();

        base.setInvasion(null);
    }

    public boolean isInvader(Player player) {
        if(clan != null) return clan.getMembers().contains(player.getName()) ||
                clan.getLeader().equalsIgnoreCase(player.getName());

        if(invasor != null) return invasor.equalsIgnoreCase(player.getName());

        return false;
    }
}