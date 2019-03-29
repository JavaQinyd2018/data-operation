package com.operation.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.operation.database.service.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/9 16:11
 * 数据库基本操作：增删改查
 */
public class Database {

    public static int insert(String tableName, Map<String, Object> paramMap) {
        return InsertHelper.insert(tableName, paramMap);
    }

    public static int insert(String sql){
        return InsertHelper.insert(sql);
    }

    public static <T> int insert(Class<T> clazz, T entity, String tableName) {
        return InsertHelper.insert(clazz, entity, tableName);
    }

    public static <T> int insertUnderline(Class<T> clazz, T entity, String tableName) {
        return InsertHelper.insertUnderline(clazz, entity, tableName);
    }

    public static int batchInsert(String csvFilePath,String tableName) {
        return InsertHelper.batchInsert(csvFilePath,tableName);
    }

    public static int batchInsertUnderline(String csvFilePath,String tableName) {
        return InsertHelper.batchInsertUnderline(csvFilePath,tableName);
    }

    public static int update(String sql) {
        return UpdateHelper.update(sql);
    }

    public static int update(String tableName, String setField, String condition) {
        return UpdateHelper.update(tableName, setField, condition);
    }

    public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap) {
        return UpdateHelper.update(tableName, paramMap, whereConditionMap);
    }

    public static int delete(String sql) {
        return DeleteHelper.delete(sql);
    }

    public static int delete(String tableName, String condition) {
        return DeleteHelper.delete(tableName, condition);
    }

    public static Map<String, Object> selectOne(String sql) {
        return SelectHelper.selectOne(sql);
    }

    public static Map<String, Object> selectOne(String tableName, String condition) {
        return SelectHelper.selectOne(tableName, condition);
    }

    public static Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap) {
        return SelectHelper.selectOne(tableName, whereConditionMap);
    }

    public static <T> T selectOne(Class<T> clazz,String tableName, String condition) {
        return SelectHelper.selectOne(clazz,tableName, condition);
    }

    public static <T> T selectOne(Class<T> clazz,String tableName,Map<String, Object> whereConditionMap) {
        return SelectHelper.selectOne(clazz,tableName, whereConditionMap);
    }

    public static List<Map<String, Object>> selectList(String sql) {
        return SelectHelper.selectList(sql);
    }

    public static List<Map<String, Object>> selectList(String tableName, String condition) {
        return SelectHelper.selectList(tableName, condition,"");
    }

    public static <T> List<T>  selectList(Class<T> clazz, String tableName, String condition) {
        return JSONArray.parseArray(JSONObject.toJSONString(selectList(tableName, condition)), clazz);
    }

    public static List<Map<String, Object>> selectList(String tableName,Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return SelectHelper.selectList(tableName, whereConditionMap, orderByCondition);
    }

    public static  <T> List<T> selectList(Class<T> clazz, String tableName,Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return SelectHelper.selectList(clazz, tableName, whereConditionMap, orderByCondition);
    }

    public static Long selectCount(String sql) {
        return SelectHelper.selectCount(sql);
    }

    public static Long selectCount(String tableName, String condition) {
        return SelectHelper.selectCount(tableName, condition);
    }

    public static List<Object> selectByField(String tableName, String field, String condition) {
        return SelectHelper.selectByField(tableName, field, condition);
    }

    public static <T> int batchInsert(String envFlag, String csvFilePath, String tableName) {
        return InsertWithConfigHelper.batchInsert(null, envFlag, csvFilePath, tableName);
    }

    public static <T> int batchInsert(String envFlag,String csvFilePath, Class<T> clazz, String tableName) {
        return InsertWithConfigHelper.batchInsert(null, envFlag, csvFilePath, clazz, tableName);
    }

    public static int update(String envFlag, String sql) {
        return UpdateWithConfigHelper.update(null, envFlag,sql);
    }

    public static int update(String envFlag, String tableName, String setField, String condition) {
        return UpdateWithConfigHelper.update(null, envFlag, tableName, setField, condition);
    }


    public static int delete(String envFlag, String tableName, String condition) {
        return DeleteHelper.delete(null, envFlag, tableName, condition);
    }

    public static Map<String, Object> selectOne(String envFlag, String tableName, String condition) {
        return SelectWithConfigHelper.selectOne(null, envFlag, tableName, condition);
    }

    public static <T> T selectOne(String envFlag, Class<T> clazz, String tableName, String condition) {
        return SelectWithConfigHelper.selectOne(null, envFlag,clazz,tableName, condition);
    }

    public static <T> List<T> selectList(String envFlag, Class<T> clazz, String tableName, String condition) {
        List<Map<String, Object>> maps = SelectWithConfigHelper.selectList(null, envFlag, tableName, condition, "");
        return JSONArray.parseArray(JSON.toJSONString(maps),clazz);
    }

    public static Long selectCount(String envFlag, String tableName, String condition) {
        return SelectWithConfigHelper.selectCount(null, envFlag, tableName, condition);
    }


}
