package com.vasconcellos.voxybases.dao.impl;

import com.vasconcellos.voxybases.DataSource;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.dao.SafeDao;
import com.vasconcellos.voxybases.object.Safe;
import lombok.SneakyThrows;
import org.bukkit.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SafeDaoImpl implements SafeDao {

    private final File folder;

    public SafeDaoImpl() {
        folder = new File(VoxyBases.getInstance()
                .getDataFolder(), "cofres");

        if(!folder.exists()) folder.mkdirs();
    }

    @Override
    public void save(Safe safe) {
        try (PrintWriter out = new PrintWriter(safe.getFile())) {
            out.println(DataSource.getGson().toJson(safe, Safe.class));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(Safe safe) {
        safe.getFile().delete();
    }

    @Override
    public Map<Location, Safe> findAll() {
        Map<Location, Safe> safes = new HashMap<>();

        for(File file : folder.listFiles())
            if(file.getName().endsWith(".json")) {
                Safe safe = load(file);
                safes.put(safe.getLocation(), safe);
            }

        return safes;
    }

    @SneakyThrows
    private Safe load(File file) {
        Safe safe = DataSource.getGson().fromJson(new String(Files
                .readAllBytes(file.toPath())), Safe.class);
        safe.setFile(file);

        return safe;
    }
}