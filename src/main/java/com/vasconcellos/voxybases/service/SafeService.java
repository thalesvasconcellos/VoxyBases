package com.vasconcellos.voxybases.service;

import com.vasconcellos.voxybases.dao.SafeDao;
import com.vasconcellos.voxybases.dao.impl.SafeDaoImpl;
import com.vasconcellos.voxybases.object.Safe;
import lombok.Getter;
import org.bukkit.Location;

import java.util.Map;

public class SafeService {

    @Getter private static SafeDao dao;

    public SafeService() {
        dao = new SafeDaoImpl();
    }

    public void save(Safe safe) {
        dao.save(safe);
    }

    public Map<Location, Safe> findAll() {
        return dao.findAll();
    }

    public void delete(Safe safe) {
        dao.delete(safe);
    }
}