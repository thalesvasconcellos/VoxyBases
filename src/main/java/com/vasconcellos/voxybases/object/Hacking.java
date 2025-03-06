package com.vasconcellos.voxybases.object;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Data
public class Hacking {

    private final long startedAt = System.currentTimeMillis();

    private final Base base;

    private final Safe safe;
    private final Player hacker;

    private BukkitTask task;
    private int time;

    public void start() {
        safe.setState(SafeState.HACKING);
        safe.setHacking(this);

        task = new BukkitRunnable() {

            @Override
            public void run() {
                if(getEnd() - System.currentTimeMillis() < 1000L) {
                    if(safe instanceof ClanSafe && base.getInvasion() != null) base.getInvasion().setSafeHacked(true);

                    safe.setState(SafeState.HACKED);
                    safe.updateHologram();

                    time = 300;

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            time--;

                            if(safe.getHacking() == null) cancel();

                            if(time <= 0) {
                                hacker.closeInventory();

                                safe.setState(SafeState.NORMAL);
                                safe.setHacking(null); cancel();
                            }

                            safe.updateHologram();
                        }
                    }.runTaskTimer(VoxyBases.getInstance(), 0L, 20L);

                    cancel();
                }

                safe.updateHologram();
            }
        }.runTaskTimer(VoxyBases.getInstance(), 0L, 20L);
    }

    public void stop(boolean taked) {
        task.cancel();

        safe.setState(taked ? SafeState.TAKED : SafeState.INTERRUPTED);
        safe.updateHologram();

        new BukkitRunnable() {
            @Override
            public void run() {
                safe.setHacking(null);
                safe.setState(SafeState.NORMAL);

                safe.updateHologram();
            }
        }.runTaskLater(VoxyBases.getInstance(), (safe instanceof PersonalSafe ? SafeValue.get(
                SafeValue::safeStopHologramTime) :
                BaseValue.get(BaseValue::safeStopHologramTime)
        ) * 20L);
    }

    public void stop() {
        stop(false);
    }
    public long getEnd() {
        return startedAt + safe.getTime();
    }

    public boolean isHacker(Player player) {
        return hacker.getUniqueId().equals(player.getUniqueId());
    }
}