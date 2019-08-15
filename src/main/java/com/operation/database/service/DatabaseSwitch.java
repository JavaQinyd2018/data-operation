package com.operation.database.service;

import com.operation.database.entity.SqlType;
import com.operation.database.utils.PreCheckUtils;
import com.operation.database.utils.SqlParser;

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
        SqlParser.checkSql(sql, SqlType.INSERT);
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(sql);
    }

    public static int insert(String env, String tableName, Map<String, Object> paramMap){
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(paramMap, "插入的参数集合不能为空");
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(tableName, paramMap);
    }

    public static int[] batchInsert(String env, String tableName, List<Map<String, Object>> paramMapList) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(paramMapList, "插入的参数集合不能为空");
        InsertExecutor executor = new InsertExecutor(env);
        return executor.batchInsert(tableName, paramMapList);
    }

    public static int[] batchInsertUnderline(String env, String csvFilePath,String tableName) {
        PreCheckUtils.checkEmpty(csvFilePath, "csv文件的路径不能为空");
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        InsertExecutor executor = new InsertExecutor(env);
        return executor.batchInsertUnderline(csvFilePath, tableName);
    }

    public static <T> int insert(String env, Class<T> clazz, T entity, String tableName) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insert(clazz, entity, tableName);
    }

    public static <T> int insertUnderline(String env, Class<T> clazz, T entity, String tableName) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        InsertExecutor executor = new InsertExecutor(env);
        return executor.insertUnderline(clazz, entity, tableName);
    }

    public static int update(String env, String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        UpdateExecutor executor = new UpdateExecutor(env);
        return executor.update(sql);
    }

    public static int update(String env, String tableName, String setField, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(condition, "条件不能为空");
        UpdateExecutor executor = new UpdateExecutor(env);
        return executor.update(tableName, setField, condition);
    }

    public static int update(String env, String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap){
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(whereConditionMap, "条件集合不能为空");
        UpdateExecutor updateExecutor = new UpdateExecutor(env);
        return updateExecutor.update(tableName, paramMap, whereConditionMap);
    }

    public static int delete(String env, String sql){
        SqlParser.checkSql(sql, SqlType.DELETE);
        DeleteExecutor executor = new DeleteExecutor(env);
        return executor.delete(sql);
    }

    public static int delete(String env, String tableName, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(condition, "条件不能为空");
        DeleteExecutor executor = new DeleteExecutor(env);
        return executor.delete(tableName, condition);
    }

    public static int delete(String env, String tableName, Map<String, Object> deleteMap){
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(deleteMap, "条件集合不能为空");
        DeleteExecutor executor= new DeleteExecutor(env);
        return executor.delete(tableName, deleteMap);
    }

    public static Map<String, Object> selectOne(String env, String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(sql);
    }


    public static Map<String, Object> selectOne(String env, String tableName, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(tableName, condition);
    }

    public static <T> T selectOne(String env, Class<T> clazz,String tableName, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, tableName, condition);
    }

    public static <T> T selectOne(String env, Class<T> clazz, String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, sql);
    }

    public static Map<String, Object> selectOne(String env, String tableName,Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(tableName, whereConditionMap);
    }


    public static <T> T selectOne(String env, Class<T> clazz, String tableName,Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectOne(clazz, tableName, whereConditionMap);
    }


    public static List<Map<String, Object>> selectList(String env, String tableName, String whereCondition, String orderByCondition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(tableName, whereCondition, orderByCondition);
    }

    public static List<Map<String,Object>> selectList(String env, String sql) {
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(sql);
    }


    public static List<Map<String,Object>> selectList(String env, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(tableName, whereConditionMap, orderByCondition);
    }

    public static <T> List<T> selectList(String env, Class<T> clazz,String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectList(clazz, sql);
    }

    public static <T> List<T> selectList(String env, Class<T> clazz, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor selectExecutor = new SelectExecutor(env);
        return selectExecutor.selectList(clazz, tableName, whereConditionMap, orderByCondition);
    }

    public static Long selectCount(String env, String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectCount(sql);
    }

    public static Long selectCount(String env, String tableName, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectCount(tableName, condition);
    }

    /**
     * 根据列查询
     * @param env
     * @param tableName
     * @param column 可以是一列（username），也可以是多列（username,password,email）--用，隔开
     * @param condition
     * @return
     */
    public static List<Map<String, Object>> selectColumn(String env, String tableName, String column, String condition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(column, "字段不能为空");
       SelectExecutor executor = new SelectExecutor(env);
       return executor.selectColumn(tableName, column, condition);
    }

    public static List<Map<String, Object>> selectColumn(String env, String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                           Map<String, String> orderByCondition) {
        PreCheckUtils.checkEmpty(tableName, "表名不能为空");
        PreCheckUtils.checkEmpty(fieldList, "查询字段不能为空");
        SelectExecutor executor = new SelectExecutor(env);
        return executor.selectListByFieldList(tableName, fieldList, whereConditionMap, orderByCondition);
    }
}
