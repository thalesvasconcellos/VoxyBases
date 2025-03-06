package com.vasconcellos.voxybases.dao;

import com.vasconcellos.voxybases.object.Safe;
import org.bukkit.Location;

import java.util.Map;

public interface SafeDao {

    void save(Safe safe);
    void delete(Safe Safe);

    Map<Location, Safe> findAll();
}