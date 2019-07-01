package com.operation.database.service;

import com.operation.database.base.StatementService;
import com.operation.database.utils.CSVUtils;
import com.operation.database.utils.ReflectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/30 8:49
 * @desciption
 * @since
 */
class InsertExecutor {

    private BaseDatabaseService baseService;

    InsertExecutor(String env) {
        StatementService service = new StatementService(env);
        baseService = new BaseDatabaseService(service);
    }

    int insert(String sql){
        return baseService.insert(sql);
    }

    int insert(String tableName, Map<String, Object> paramMap){
        return baseService.insert(tableName, paramMap);
    }

    int[] batchInsert(String tableName, List<Map<String, Object>> paramMapList) {
        return baseService.batchInsert(tableName, paramMapList);
    }

    int[] batchInsertUnderline(String csvFilePath, String tableName) {
        List<Map<String, Object>> list = CSVUtils.parseCSVFileToUnderline(csvFilePath);
        return batchInsert(tableName, list);
    }

    <T> int insert(Class<T> clazz, T entity, String tableName) {
        Map<String, Object> map = ReflectUtils.getFileNameAndValueMap(clazz, entity);
        return baseService.insert(tableName, map);
    }

    <T> int insertUnderline(Class<T> clazz, T entity, String tableName) {
        Map<String, Object> map = ReflectUtils.getFileNameAndValueMap(clazz, entity);
        return baseService.insert(tableName, map);
    }
}
