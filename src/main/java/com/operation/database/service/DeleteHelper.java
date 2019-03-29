package com.operation.database.service;

import com.operation.database.entity.Configuration;
import com.operation.database.service.basic.CrudService;


/**
 * @Author: Qinyadong
 * @Date: 2019/1/9 16:07
 * @Since:
 */
public abstract class DeleteHelper {

    private static CrudService crudService = null;

    public static int delete(String sql){
        crudService = new CrudService(null, null);
        return crudService.delete(sql);
    }

    public static int delete(Configuration configuration,String envFlag, String sql){
        crudService = new CrudService(configuration, envFlag);
        return crudService.delete(sql);
    }

    public static int delete(String tableName, String condition) {
        crudService = new CrudService(null, null);
        return crudService.delete(tableName, condition);
    }

    public static int delete(Configuration configuration,String envFlag, String tableName, String condition){
        crudService = new CrudService(configuration, envFlag);
        return crudService.delete(tableName, condition);
    }
}
