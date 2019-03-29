package com.operation.database.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.operation.database.entity.Configuration;
import com.operation.database.service.basic.CrudService;
import com.operation.database.sql.SqlParser;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
public class SelectWithConfigHelper {

    private static CrudService crudService = null;

    public static Map<String, Object> selectOne(Configuration configuration, String envFlag, String sql) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectOne(sql);
    }


    public static Map<String, Object> selectOne(Configuration configuration, String envFlag, String tableName, String condition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectOne(tableName, condition);
    }

    public static <T> T selectOne(Configuration configuration, String envFlag, Class<T> clazz,String tableName, String condition) {
        return JSONObject.parseObject(JSONObject.toJSONString(selectOne(configuration, envFlag,tableName, condition)),clazz);
    }

    public static <T> T selectOne(Configuration configuration, String envFlag, Class<T> clazz, String sql) {
        return JSONObject.parseObject(JSONObject.toJSONString(selectOne(configuration, envFlag, sql)),clazz);
    }

    public static Map<String, Object> selectOne(Configuration configuration, String envFlag,String tableName,Map<String, Object> whereConditionMap) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectOne(tableName, whereConditionMap);
    }


    public static <T> T selectOne(Configuration configuration, String envFlag, Class<T> clazz, String tableName,Map<String, Object> whereConditionMap) {
        Map<String, Object> map = selectOne(configuration, envFlag, tableName, whereConditionMap);
        return JSONObject.parseObject(JSONObject.toJSONString(map),clazz);
    }


    public static List<Map<String, Object>> selectList(Configuration configuration,
                                                       String envFlag,
                                                       String tableName,
                                                       String whereCondition,
                                                       String orderByCondition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectList(tableName,whereCondition,orderByCondition);
    }

    public static List<Map<String,Object>> selectList(Configuration configuration, String envFlag, String sql) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectList(sql);
    }


    public static List<Map<String,Object>> selectList(Configuration configuration,
                                                      String envFlag,
                                                      String tableName,
                                                      Map<String, Object> whereConditionMap,
                                                      Map<String, String> orderByCondition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectList(tableName, whereConditionMap, orderByCondition);
    }

    public static <T> List<T> selectList(Configuration configuration,
                                         String envFlag,
                                         Class<T> clazz,
                                         String sql) {
        return JSONArray.parseArray(JSONObject.toJSONString(selectList(configuration, envFlag, sql)), clazz);
    }

    public static <T> List<T> selectList(Configuration configuration,
                                         String envFlag,
                                         Class<T> clazz,
                                         String tableName,
                                         Map<String, Object> whereConditionMap,
                                         Map<String, String> orderByCondition) {
        return JSONArray.parseArray(JSONObject.toJSONString(selectList(configuration, envFlag, tableName, whereConditionMap, orderByCondition)), clazz);
    }

    public static Long selectCount(Configuration configuration, String envFlag, String sql) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectCount(sql);
    }

    public static Long selectCount(Configuration configuration, String envFlag, String tableName, String condition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectCount(tableName,condition);
    }

    public static List<Object> selectByField(Configuration configuration,
                                             String envFlag,
                                             String tableName,
                                             String field,
                                             String condition) {
        crudService = new CrudService(configuration, envFlag);
        return crudService.selectByField(tableName, field, condition);
    }

    public static Map<String, Object> selectByFieldList(Configuration configuration,
                                                        String envFlag, String tableName,
                                                        List<String> fieldList,
                                                        Map<String, Object> whereConditionMap,
                                                        Map<String, String> orderByCondition) {
        crudService = new CrudService(configuration, envFlag);
        String sql = SqlParser.buildSelectSql(tableName, fieldList, whereConditionMap, orderByCondition);
        return crudService.selectByFieldList(fieldList, sql);
    }

    public static List<Map<String, Object>> selectListByFieldList(Configuration configuration,
                                                                  String envFlag, String tableName,
                                                                  List<String> fieldList,
                                                                  Map<String, Object> whereConditionMap,
                                                                  Map<String, String> orderByCondition) {
        crudService = new CrudService(configuration, envFlag);
        String sql = SqlParser.buildSelectSql(tableName, fieldList, whereConditionMap, orderByCondition);
        return crudService.selectListByFieldList(fieldList, sql);
    }
}
