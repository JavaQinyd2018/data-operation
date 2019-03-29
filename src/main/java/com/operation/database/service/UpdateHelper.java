package com.operation.database.service;

import com.operation.database.service.basic.CrudService;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/9 16:05
 * @Since:
 */
public abstract class UpdateHelper {

    private static CrudService crudService = new CrudService(null, null);

    public static int update(String sql) {
        return crudService.update(sql);
    }

    public static int update(String tableName, String setField, String condition) {
       return crudService.update(tableName,setField,condition);
    }

    public static int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap){
        return crudService.update(tableName, paramMap, whereConditionMap);
    }
}
