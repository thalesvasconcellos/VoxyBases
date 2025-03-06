package com.vasconcellos.voxybases.object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class GateBlock {

    private final Location location;

    private final Material type;
    private final byte data;

    public GateBlock(Block block) {
        this.location = block.getLocation();

        this.type = block.getType();
        this.data = block.getData();
    }

    public void open() {
        location.getBlock().setType(Material.AIR);
    }

    public void close() {
        location.getBlock().setType(type);
        location.getBlock().setData(data);
    }

    public Block getBlock() {
        return location.getBlock();
    }
}