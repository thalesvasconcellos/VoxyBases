package com.vasconcellos.voxybases.task;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.MoreExecutors;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.util.forcefield.ForceFieldTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegionTask implements Runnable {

    public static final Map<UUID, Collection<Location>> LASTSHOWN = Maps.newConcurrentMap();

    private final VoxyBases plugin;
    private final Set<UUID> processing = new HashSet<>();

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if (processing.contains(player.getUniqueId())) return;

            ArrayList<Block> blocks = getBlocks(player.getLocation());

            List<ProtectedRegion> regions = plugin.getBaseManager().getBases().values().stream().filter(base ->
                            blocks.stream().anyMatch(block -> base.getRegion().contains(
                                    block.getX(),
                                    block.getY(),
                                    block.getZ()
                            ))).filter(base -> !base.canEnter(player))
                    .map(Base::getRegion)
                    .collect(Collectors.toList());

            if (regions.size() > 0)
                processing.add(player.getUniqueId());
            else if (LASTSHOWN.containsKey(player.getUniqueId())) {
                processing.add(player.getUniqueId());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Location lastShown : LASTSHOWN.get(player.getUniqueId()))
                            player.sendBlockChange(lastShown, lastShown.getBlock()
                                    .getType(), lastShown.getBlock().getData());

                        LASTSHOWN.remove(player.getUniqueId());
                        processing.remove(player.getUniqueId());
                    }
                }.runTask(plugin);

                return;
            }

            ForceFieldTask task = new ForceFieldTask(player, regions);
            task.addListener(() -> processing.remove(player.getUniqueId()), MoreExecutors.sameThreadExecutor());

            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    private ArrayList<Block> getBlocks(Location start) {
        int radius = 5;

        ArrayList<Block> blocks = new ArrayList<>();

        for(double x = start.getX() - radius; x <= start.getX() + radius; x++)
            for(double y = start.getY() - radius; y <= start.getY() + radius; y++)
                for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++) {
                    Location location = new Location(start.getWorld(), x, y, z);

                    blocks.add(location.getBlock());
                }

        return blocks;
    }
}
