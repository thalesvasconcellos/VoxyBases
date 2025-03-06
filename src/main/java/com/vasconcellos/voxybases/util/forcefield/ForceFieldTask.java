package com.vasconcellos.voxybases.util.forcefield;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.task.RegionTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ForceFieldTask extends AbstractFuture implements Runnable, ListenableFuture {

    private final Player player;
    private final List<ProtectedRegion> regions;

    @Override
    public void run() {
        World world = player.getWorld();

        Set<Location> shownBlocks = new HashSet<>();

        for(ProtectedRegion region : regions)
            for (Location point : BorderFinder.getBorderPoints(world, region))
                for (double y = region.getMinimumPoint().getY(); y <= region.getMaximumPoint().getY(); y++) {
                    Location toShown = new Location(world, point.getX(), y, point.getZ());

                    int distance = distanceSquared(toShown, player.getLocation());
                    if (distance <= 50) shownBlocks.add(toShown);
                }

        Collection<Location> lastShown = RegionTask.LASTSHOWN.get(player.getUniqueId());
        if (lastShown == null) lastShown = new HashSet<>();

        for (Location noLongerShown : lastShown) {
            if (shownBlocks.contains(noLongerShown)) continue; //We will show
            player.sendBlockChange(noLongerShown, noLongerShown.getBlock().getType(),
                    noLongerShown.getBlock().getData());
        }

        for (Location toShow : shownBlocks) {
            if (toShow.getBlock().getType().isSolid()) continue;
            player.sendBlockChange(toShow, Material.STAINED_GLASS, (byte) 14);
        }

        RegionTask.LASTSHOWN.put(player.getUniqueId(), shownBlocks);
        set(null);
    }

    private int distanceSquared(Location current, Location other) {
        return square(current.getBlockX() - other.getBlockX()) + square(current.getBlockY() - other.getBlockY())
                + square(current.getBlockZ() - other.getBlockZ());
    }

    private static int square(int i) {
        return i * i;
    }
}