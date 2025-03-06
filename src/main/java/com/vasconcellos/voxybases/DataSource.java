package com.vasconcellos.voxybases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vasconcellos.voxybases.adapter.BaseAdapter;
import com.vasconcellos.voxybases.adapter.SafeAdapter;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Safe;
import lombok.Getter;

public class DataSource {

    @Getter private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();

        builder.setPrettyPrinting();

        builder.registerTypeAdapter(Safe.class, new SafeAdapter());
        builder.registerTypeAdapter(Base.class, new BaseAdapter());

        gson = builder.create();
    }
}