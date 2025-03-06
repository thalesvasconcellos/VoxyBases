package com.vasconcellos.voxybases.manager;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Gate;
import com.vasconcellos.voxybases.object.GateBlock;
import com.vasconcellos.voxybases.object.Safe;
import lombok.Getter;
import lombok.Setter;
import net.voxymc.clans.model.Clan;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BaseManager {

    @Getter @Setter private Map<String, Base> bases = new HashMap<>();

    public void add(Base base) {
        bases.put(base.getRegion().getId().toLowerCase(), base);
    }

    public void remove(Base base) {
        bases.remove(base.getRegion().getId().toLowerCase());
    }

    public Base getById(String id) {
        return bases.get(id.toLowerCase());
    }

    public Base getByClan(Clan clan) {
        return bases.values().stream().filter(base -> base.getClan() != null && base
                .getClan().equals(clan)).findAny().orElse(null);
    }

    public void getByBlock(Block block, BiConsumer<Base,  Gate> consumer) {
        for(Base base : bases.values())
            for(Gate gate : base.getGates().values())
                for(List<GateBlock> step : gate.getSteps().values())
                    for(GateBlock gateBlock : step)
                        if(gateBlock.getBlock().equals(block))
                            consumer.accept(base, gate);
    }

    public Base getBySafe(Safe safe) {
        for(Base base : bases.values())
            if(base.getSafe().equals(safe))
                return base;

        return null;
    }

    public boolean isBase(ProtectedRegion region) {
        return bases.values().stream().anyMatch(base -> base.getRegion().equals(region));
    }

    public Base getByRegion(ProtectedRegion region) {
        return bases.values().stream().filter(base -> base.getRegion()
                .equals(region)).findAny().orElse(null);
    }
}