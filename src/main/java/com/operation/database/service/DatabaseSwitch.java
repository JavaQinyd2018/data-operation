package com.operation.database.service;

import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 23:19
 * @desciption 数据源配置切换操作类
 * @since
 */
public final class DatabaseSwitch {

    private DatabaseSwitch() {}

    public static int insert(String env, String sql){
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(sql);
    }

    public static int insert(String env, String tableName, Map<String, Object> paramMap){
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(tableName, paramMap);
    }

    public static int[] batchInsert(String env, String tableName, List<Map<String, Object>> paramMapList) {
        InsertExecutor executor = new InsertExecutor(env);
        return executor.batchInsert(tableName, paramMapList);
    }

    public static int[] batchInsertUnderline(String env, String csvFilePath,String tableName) {
        InsertExecutor executor = new InsertExecutor(env);
        return executor.batchInsertUnderline(csvFilePath, tableName);
    }

    public static <T> int insert(String env, Class<T> clazz, T entity, String tableName) {
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(clazz, entity, tableName);
    }

    public static <T> int insertUnderline(String env, Class<T> clazz, T entity, String tableName) {
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insertUnderline(clazz, entity, tableName);
    }

    public static int update(String env, String sql) {
        UpdateExecutor executor = new UpdateExecutor(env);
        return executor.update(sql);
    }

    public static int update(String env, String tableName, String setField, String condition) {
        UpdateExecutor executor = new UpdateExecutor(env);
        return executor.update(tableName, setField, condition);
    }

    public static int update(String env, String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap){
        UpdateExecutor updateExecutor = new UpdateExecutor(env);
        return updateExecutor.update(tableName, paramMap, whereConditionMap);
    }

    public static int delete(String env, String sql){
        DeleteExecutor executor = new DeleteExecutor(env);
        return executor.delete(sql);
    }

    public static int delete(String env, String tableName, String condition) {
        DeleteExecutor executor = new DeleteExecutor(env);
        return executor.delete(tableName, condition);
    }

    public static int delete(String env, String tableName, Map<String, Object> deleteMap){
        DeleteExecutor executor= new DeleteExecutor(env);
        return executor.delete(tableName, deleteMap);
    }

    public static Map<String, Object> selectOne(String env, String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(sql);
    }


    public static Map<String, Object> selectOne(String env, String tableName, String condition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(tableName, condition);
    }

    public static <T> T selectOne(String env, Class<T> clazz,String tableName, String condition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, tableName, condition);
    }

    public static <T> T selectOne(String env, Class<T> clazz, String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, sql);
    }

    public static Map<String, Object> selectOne(String env, String tableName,Map<String, Object> whereConditionMap) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(tableName, whereConditionMap);
    }


    public static <T> T selectOne(String env, Class<T> clazz, String tableName,Map<String, Object> whereConditionMap) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, tableName, whereConditionMap);
    }


    public static List<Map<String, Object>> selectList(String env, String tableName, String whereCondition, String orderByCondition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(tableName, whereCondition, orderByCondition);
    }

    public static List<Map<String,Object>> selectList(String env, String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(sql);
    }


    public static List<Map<String,Object>> selectList(String env, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(tableName, whereConditionMap, orderByCondition);
    }

    public static <T> List<T> selectList(String env, Class<T> clazz,String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(clazz, sql);
    }

    public static <T> List<T> selectList(String env, Class<T> clazz, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        SelectExecutor selectExecutor = new SelectExecutor(env);
        return selectExecutor.selectList(clazz, tableName, whereConditionMap, orderByCondition);
    }

    public static Long selectCount(String env, String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectCount(sql);
    }

    public static Long selectCount(String env, String tableName, String condition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectCount(tableName, condition);
    }

    public static List<Map<String, Object>> selectByField(String env, String tableName, String field, String condition) {
       SelectExecutor executor = new SelectExecutor(env);
       return executor.selectByField(tableName, field, condition);
    }

    public static Map<String, Object> selectByFieldList(String env, String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                 Map<String, String> orderByCondition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectByFieldList(tableName, fieldList, whereConditionMap, orderByCondition);
    }

    public static List<Map<String, Object>> selectListByFieldList(String env, String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                           Map<String, String> orderByCondition) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectListByFieldList(tableName, fieldList, whereConditionMap, orderByCondition);
    }
}
