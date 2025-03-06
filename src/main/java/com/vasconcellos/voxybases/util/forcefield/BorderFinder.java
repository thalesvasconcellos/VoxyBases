package com.vasconcellos.voxybases.util.forcefield;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BorderFinder {

    public static Collection<Location> getBorderPoints(World world, ProtectedRegion region) {
        HashSet<Location> result = new HashSet<>();

        for(Location point : getPoints(world, region)) {
            getAlongX(point, world, region, result);
            getAlongZ(point, world, region, result);
        }

        getBottomOrTop(region.getMinimumPoint(), world, region, result);
        getBottomOrTop(region.getMaximumPoint(), world, region, result);

        return result;
    }

    private static void getBottomOrTop(BlockVector bottomOrTop, World world, ProtectedRegion region, HashSet<Location> result) {
        for (int x = bottomOrTop.getBlockX(); region.contains(x, bottomOrTop.getBlockY(), bottomOrTop.getBlockZ()); x++)
            for (int z = bottomOrTop.getBlockZ(); region.contains(x, bottomOrTop.getBlockY(), z); z++)
                result.add(new Location(world, x, bottomOrTop.getBlockY(), z));
    }

    private static void getAlongX(Location start, World world, ProtectedRegion region, HashSet<Location> result) {
        if (region.contains(start.getBlockX() + 1, start.getBlockY(), start.getBlockZ()))
            for (int x = start.getBlockX(); region.contains(x, start.getBlockY(), start.getBlockZ()); x++)
                result.add(new Location(world, x, start.getBlockY(), start.getBlockZ()));
        else for (int x = start.getBlockX(); region.contains(x, start.getBlockY(), start.getBlockZ()); x--)
                result.add(new Location(world, x, start.getBlockY(), start.getBlockZ()));
    }

    private static void getAlongZ(Location start, World world, ProtectedRegion region, HashSet<Location> result) {
        if (region.contains(start.getBlockX(), start.getBlockY(), start.getBlockZ() + 1))
            for (int z = start.getBlockZ(); region.contains(start.getBlockX(), start.getBlockY(), z); z++)
                result.add(new Location(world, start.getBlockX(), start.getBlockY(), z));
        else for (int z = start.getBlockZ(); region.contains(start.getBlockX(), start.getBlockY(), z); z--)
                result.add(new Location(world, start.getBlockX(), start.getBlockY(), z));
    }
    
    private static Collection<Location> getPoints(World world, ProtectedRegion region) {
        List<BlockVector2D> rawPoints = region.getPoints();
        Set<Location> points = new HashSet<>();

        int y = region.getMinimumPoint().getBlockY();

        for (BlockVector2D point : rawPoints)
            points.add(new Location(world, point.getBlockX(), y, point.getBlockZ()));

        return points;
    }
}