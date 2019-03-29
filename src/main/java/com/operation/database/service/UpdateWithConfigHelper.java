package com.operation.database.service;

import com.operation.database.entity.Configuration;
import com.operation.database.service.basic.CrudService;

import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
public class UpdateWithConfigHelper {

    private static CrudService crudService = null;

    public static int update(Configuration configuration, String envFlag, String sql) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.update(sql);
    }

    public static int update(Configuration configuration,
                             String envFlag,
                             String tableName,
                             String setField,
                             String condition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.update(tableName,setField,condition);
    }

    public static int update(Configuration configuration,
                             String envFlag,
                             String tableName,
                             Map<String, Object> paramMap,
                             Map<String, Object> whereConditionMap){
        crudService = new CrudService(configuration, envFlag);
        return crudService.update(tableName, paramMap, whereConditionMap);
    }
}
