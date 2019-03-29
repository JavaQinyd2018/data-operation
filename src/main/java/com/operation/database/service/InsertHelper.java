package com.operation.database.service;

import com.alibaba.fastjson.JSONObject;
import com.operation.database.service.basic.CrudService;
import com.operation.database.utils.CSVUtils;
import com.operation.database.utils.CamelToUnderlineUtil;
import com.operation.database.utils.ObjectReflectUtils;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.testng.collections.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/9 14:17
 * @Since:
 */
public abstract class InsertHelper {

    private static CrudService crudService = new CrudService(null, null);


    private static final List<String> CLASSNAME_LIST = Arrays.asList("boolean","byte","char","short","int","long","float","double",
            "java.lang.Boolean","java.lang.Byte","java.lang.Character","java.lang.Short","java.lang.Integer",
            "java.lang.Long","java.lang.Float","java.lang.Double","java.lang.Enum","java.util.Date","java.lang.String",
            "java.sql.Date");

    public static int insert(String sql) {
        return crudService.insert(sql);
    }

    public static int insert(String tableName, Map<String, Object> paramMap) {
        return crudService.insert(tableName, paramMap);
    }


    public static int[] batchInsert(String tableName, List<Map<String, Object>> paramMapList) {
        return crudService.batchInsert(tableName, paramMapList);
    }

    public static int batchInsert(String csvFilePath,String tableName) {
        List<Map<String, Object>> mapList = CSVUtils.parseCSVFile(csvFilePath);
        return Arrays.stream(batchInsert(tableName, mapList)).sum();
    }

    public static int batchInsertUnderline(String csvFilePath,String tableName) {
        List<Map<String, Object>> mapList = CSVUtils.parseCSVFileToUnderline(csvFilePath);
        return Arrays.stream(batchInsert(tableName, mapList)).sum();
    }

    public static <T> int insert(Class<T> clazz, T entity, String tableName) {
        List<String> fieldNameList = ObjectReflectUtils.getFieldNameList(clazz);
        Map<String, Object> map = Maps.newLinkedHashMap();
        fieldNameList.forEach(fieldName -> {
            Object o = ObjectReflectUtils.invokeGetMethod(entity, clazz, fieldName);
            if (o == null) {return;}
            map.put(fieldName, o);
        });
        return insert(tableName, map);
    }

    public static <T> int insertUnderline(Class<T> clazz, T entity, String tableName) {
        List<String> fieldNameList = ObjectReflectUtils.getFieldNameList(clazz);
        Map<String, Object> map = Maps.newLinkedHashMap();
        fieldNameList.forEach(fieldName -> {
            Object o = ObjectReflectUtils.invokeGetMethod(entity, clazz, fieldName);
            if (o == null) {return;}
            map.put(CamelToUnderlineUtil.camelToUnderline(fieldName), o);
        });
        return insert(tableName, map);
    }

    /**
     * 混合插入：对象中嵌套对象
     * @param csvFilePath 行式csv
     * @param clazz
     * @param tableName
     * @param <T>
     * @return
     */
    public static <T> int batchInsertHybrid(String csvFilePath, Class<T> clazz, String tableName) {
        List<Map<String, Object>> mapList = CSVUtils.parseCSVFile(csvFilePath);
        List<T> entityList = buildEntityList(clazz, mapList);
        int[] result = new int[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            result[i] = insert(clazz, entityList.get(i), tableName);
        }
        return Arrays.stream(result).sum();
    }

    private static <T> List<T> buildEntityList(Class<T> clazz, List<Map<String, Object>> mapList) {
        List<T> list = Lists.newArrayList();
        if (clazz == null || CollectionUtils.isEmpty(mapList)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        Map<String, Class<?>> nameTypeMap = ObjectReflectUtils.getFieldNameTypeMap(clazz);
        for (Map<String, Object> map : mapList) {
            for (Map.Entry<String, Class<?>> entry : nameTypeMap.entrySet()) {
                if (InsertHelper.CLASSNAME_LIST.contains(entry.getValue().getName()) || Enum.class.isAssignableFrom(entry.getValue())) {
                    continue;
                }
                Object o = JSONObject.parseObject((String) map.get(entry.getKey()), entry.getValue());
                map.put(entry.getKey(), o);
            }
            T entity = JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
            list.add(entity);
        }
        return list;
    }
}
