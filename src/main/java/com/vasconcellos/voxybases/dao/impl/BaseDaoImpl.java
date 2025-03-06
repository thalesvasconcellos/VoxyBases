package com.vasconcellos.voxybases.dao.impl;

import com.vasconcellos.voxybases.DataSource;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.dao.BaseDao;
import com.vasconcellos.voxybases.object.Base;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BaseDaoImpl implements BaseDao {

    private final File folder;

    public BaseDaoImpl() {
        folder = new File(VoxyBases.getInstance()
                .getDataFolder(), "bases");

        if(!folder.exists()) folder.mkdirs();
    }

    @Override
    public void save(Base base) {
        try (PrintWriter out = new PrintWriter(base.getFile())) {
            out.println(DataSource.getGson().toJson(base, Base.class));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(Base base) {
        base.getFile().delete();
    }

    @Override
    public Map<String, Base> findAll() {
        Map<String, Base> bases = new HashMap<>();

        for(File file : folder.listFiles())
            if(file.getName().endsWith(".json")) {
                Base base = load(file);
                bases.put(base.getRegion().getId(), base);
            }

        return bases;
    }

    @SneakyThrows
    private Base load(File file) {
        Base base = DataSource.getGson().fromJson(new String(Files
                .readAllBytes(file.toPath())), Base.class);
        base.setFile(file);

        return base;
    }
}