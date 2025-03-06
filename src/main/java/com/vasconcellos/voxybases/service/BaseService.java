package com.vasconcellos.voxybases.service;

import com.vasconcellos.voxybases.dao.BaseDao;
import com.vasconcellos.voxybases.dao.impl.BaseDaoImpl;
import com.vasconcellos.voxybases.object.Base;
import lombok.Getter;

import java.util.Map;

public class BaseService {

    @Getter private static BaseDao dao;

    public BaseService() {
        dao = new BaseDaoImpl();
    }

    public void save(Base base) {
        dao.save(base);
    }

    public Map<String, Base> findAll() {
        return dao.findAll();
    }

    public void delete(Base base) {
        dao.delete(base);
    }
}