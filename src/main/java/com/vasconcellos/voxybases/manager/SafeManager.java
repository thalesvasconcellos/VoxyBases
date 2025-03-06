package com.vasconcellos.voxybases.manager;

import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import lombok.Setter;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class SafeManager {

    @Setter private Map<Location, Safe> safes = new HashMap<>();

    public void add(Safe safe) {
        safes.put(safe.getLocation(), safe);
    }

    public void remove(Safe safe) {
        safes.remove(safe.getLocation());
    }

    public Safe get(Location location) {
        return safes.get(location);
    }

    public List<Safe> getSafesByOwner(UUID owner) {
        return safes.values().stream().filter(safe -> safe instanceof PersonalSafe)
                .map(safe -> (PersonalSafe) safe).filter(safe -> safe.getOwner()
                        .equals(owner)).collect(Collectors.toList());
    }

    public Safe getById(UUID id) {
        return safes.values().stream().filter(safe -> safe.getId()
                .equals(id)).findAny().orElse(null);
    }

    public Collection<Safe> getAll() {
        return safes.values();
    }
}