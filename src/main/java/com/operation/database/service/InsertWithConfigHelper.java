package com.operation.database.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.operation.database.entity.Configuration;
import com.operation.database.service.basic.CrudService;
import com.operation.database.utils.CSVUtils;
import com.operation.database.utils.CamelToUnderlineUtil;
import com.operation.database.utils.ObjectReflectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
public class InsertWithConfigHelper {

    private static CrudService crudService = null;

    public static int insert(Configuration configuration, String envFlag, String sql) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.insert(sql);
    }

    public static int insert(Configuration configuration, String envFlag,String tableName, Map<String, Object> paramMap) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.insert(tableName, paramMap);
    }


    public static int[] batchInsert(Configuration configuration,
                                    String envFlag,
                                    String tableName,
                                    List<Map<String, Object>> paramMapList) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.batchInsert(tableName, paramMapList);
    }

    public static int batchInsert(Configuration configuration, String envFlag,String csvFilePath,String tableName) {
        List<Map<String, Object>> mapList = CSVUtils.parseCSVFile(csvFilePath);
        return Arrays.stream(batchInsert(configuration,envFlag,tableName, mapList)).sum();
    }

    public static <T> int batchInsert(Configuration configuration, String envFlag,String csvFilePath, Class<T> clazz, String tableName) {

        List<Map<String, Object>> mapList = CSVUtils.parseCSVFile(csvFilePath);
        List<T> entityList = JSONArray.parseArray(JSON.toJSONString(mapList), clazz);
        int[] result = new int[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            result[i] = insert(configuration, envFlag, clazz, entityList.get(i), tableName);
        }
        return Arrays.stream(result).sum();
    }

    public static int batchInsertUnderline(Configuration configuration, String envFlag,String csvFilePath,String tableName) {
        List<Map<String, Object>> mapList = CSVUtils.parseCSVFileToUnderline(csvFilePath);
        return Arrays.stream(batchInsert(configuration,envFlag, tableName, mapList)).sum();
    }

    public static <T> int insert(Configuration configuration, String envFlag, Class<T> clazz, T entity, String tableName) {
        List<String> fieldNameList = ObjectReflectUtils.getFieldNameList(clazz);
        Map<String, Object> map = Maps.newLinkedHashMap();
        fieldNameList.forEach(fieldName -> {
            Object o = ObjectReflectUtils.invokeGetMethod(entity, clazz, fieldName);
            if (o == null) {return;}
            map.put(fieldName, o);
        });
        return insert(configuration,envFlag,tableName, map);
    }

    public static <T> int insertUnderline(Configuration configuration, String envFlag, Class<T> clazz, T entity, String tableName) {
        List<String> fieldNameList = ObjectReflectUtils.getFieldNameList(clazz);
        Map<String, Object> map = Maps.newLinkedHashMap();
        fieldNameList.forEach(fieldName -> {
            Object o = ObjectReflectUtils.invokeGetMethod(entity, clazz, fieldName);
            if (o == null) {return;}
            map.put(CamelToUnderlineUtil.camelToUnderline(fieldName), o);
        });
        return insert(configuration,envFlag,tableName, map);
    }
}
