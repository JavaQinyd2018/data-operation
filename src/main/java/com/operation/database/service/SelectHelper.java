package com.operation.database.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.operation.database.service.basic.CrudService;
import com.operation.database.sql.SqlParser;
import java.util.List;
import java.util.Map;


/**
 * @Author: Qinyadong
 * @Date: 2019/1/8 19:23
 * 数据库增删改查类
 */
public abstract class SelectHelper {

    /**
     * 走默认的配置
     */
    private static CrudService crudService = new CrudService(null, null);

    public static Map<String, Object> selectOne(String sql) {
        return crudService.selectOne(sql);
    }


    public static Map<String, Object> selectOne(String tableName, String condition) {
        return crudService.selectOne(tableName, condition);
    }

    public static <T> T selectOne(Class<T> clazz,String tableName, String condition) {
        return JSONObject.parseObject(JSONObject.toJSONString(selectOne(tableName, condition)),clazz);
    }

    public static <T> T selectOne(Class<T> clazz, String sql) {
        return JSONObject.parseObject(JSONObject.toJSONString(selectOne(sql)),clazz);
    }

    public static Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap) {
        return crudService.selectOne(tableName, whereConditionMap);
    }


    public static <T> T selectOne(Class<T> clazz, String tableName,Map<String, Object> whereConditionMap) {
        Map<String, Object> map = selectOne(tableName, whereConditionMap);
        return JSONObject.parseObject(JSONObject.toJSONString(map),clazz);
    }


    public static List<Map<String, Object>> selectList(String tableName, String whereCondition, String orderByCondition) {
        return crudService.selectList(tableName,whereCondition,orderByCondition);
    }

    public static List<Map<String,Object>> selectList(String sql) {
        return crudService.selectList(sql);
    }


    public static List<Map<String,Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return crudService.selectList(tableName, whereConditionMap, orderByCondition);
    }

    public static <T> List<T> selectList(Class<T> clazz,String sql) {
        return JSONArray.parseArray(JSONObject.toJSONString(selectList(sql)), clazz);
    }

    public static <T> List<T> selectList(Class<T> clazz, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return JSONArray.parseArray(JSONObject.toJSONString(selectList(tableName, whereConditionMap, orderByCondition)), clazz);
    }

    public static Long selectCount(String sql) {
        return crudService.selectCount(sql);
    }

    public static Long selectCount(String tableName, String condition) {
        return crudService.selectCount(tableName,condition);
    }

    public static List<Object> selectByField(String tableName, String field, String condition) {
        return crudService.selectByField(tableName, field, condition);
    }

    public static Map<String, Object> selectByFieldList(String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                 Map<String, String> orderByCondition) {
        String sql = SqlParser.buildSelectSql(tableName, fieldList, whereConditionMap, orderByCondition);
        return crudService.selectByFieldList(fieldList, sql);
    }

    public static List<Map<String, Object>> selectListByFieldList(String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                        Map<String, String> orderByCondition) {
        String sql = SqlParser.buildSelectSql(tableName, fieldList, whereConditionMap, orderByCondition);
        return crudService.selectListByFieldList(fieldList, sql);
    }
}
