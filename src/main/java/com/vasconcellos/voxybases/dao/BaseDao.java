package com.vasconcellos.voxybases.dao;

import com.vasconcellos.voxybases.object.Base;

import java.util.Map;

public interface BaseDao {

    void save(Base base);
    void delete(Base base);

    Map<String, Base> findAll();
}