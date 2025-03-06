package com.vasconcellos.voxybases.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.util.LocationUtils;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Gate {

    private final Location min, max;

    private Map<Integer, List<GateBlock>> steps = new HashMap<>();
    private BukkitTask task;

    public Gate(Location min, Location max) {
        this.min = min; this.max = max;

        for(int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            List<GateBlock> blocks = new ArrayList<>();

            for(int x = min.getBlockX(); x <= max.getBlockX(); x++)
                for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = min.getWorld().getBlockAt(x, y, z);
                    blocks.add(new GateBlock(block));
                }

            steps.put(y - (min.getBlockY() - 1), blocks);
        }
    }

    public void open(boolean animation, Runnable progress) {
        if(animation) {
            task = new BukkitRunnable() {
                int step = 1;

                @Override
                public void run() {
                    if(step > steps.size()) {
                        cancel();
                        task = null;

                        if(progress != null) progress.run();

                        return;
                    } steps.get(step++).forEach(GateBlock::open);
                }
            }.runTaskTimer(VoxyBases.getInstance(), 10L, 10L);
        } else steps.values().forEach(blocks -> blocks.forEach(GateBlock::open));
    }

    public void close(boolean animation, Runnable progress) {
        if(animation) {
            task = new BukkitRunnable() {
                int step = steps.size();

                @Override
                public void run() {
                    if(step <= 0) {
                        cancel();
                        task = null;

                        if(progress != null) progress.run();

                        return;
                    } steps.get(step--).forEach(GateBlock::close);
                }
            }.runTaskTimer(VoxyBases.getInstance(), 10L, 10L);
        } else steps.values().forEach(blocks -> blocks.forEach(GateBlock::close));
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        object.add("min", LocationUtils.serialize(min));
        object.add("max", LocationUtils.serialize(max));

        return object;
    }

    public static Gate fromJson(JsonObject element) {
        return new Gate(LocationUtils.deserialize(element.get("min")),
                LocationUtils.deserialize(element.get("max")));
    }
}