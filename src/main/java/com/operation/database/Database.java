package com.operation.database;

import com.operation.database.service.DatabaseSwitch;

import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 23:19
 * @desciption
 * 1. 数据库增删改查的工具类，默认读取 resources目录下面的config/db.properties数据库配置文件
 * 2. 数据库信息driver：jdbc.datasource.driver
 * 3. 数据库信息url：jdbc.datasource.url
 * 4. 数据库信息username：jdbc.datasource.username
 * 5. 数据库信息password：jdbc.datasource.password
 * @see com.operation.database.base.DatasourceConfig;
 * 需要根据环境切换数据源的提供了
 * @see com.operation.database.service.DatabaseSwitch， 可以根据env切换数据源
 * @since
 */
public final class Database {

    private Database() {}

    public static int insert(String sql){
        return DatabaseSwitch.insert("",sql);
    }

    public static int insert(String tableName, Map<String, Object> paramMap){
        return DatabaseSwitch.insert("",tableName, paramMap);
    }

    public static int[] batchInsert(String tableName, List<Map<String, Object>> paramMapList) {
        return DatabaseSwitch.batchInsert("", tableName, paramMapList);
    }

    public static int[] batchInsertUnderline(String csvFilePath,String tableName) {
        return DatabaseSwitch.batchInsertUnderline("", csvFilePath, tableName);
    }

    public static <T> int insert(Class<T> clazz, T entity, String tableName) {
        return DatabaseSwitch.insert("", clazz, entity, tableName);
    }

    public static <T> int insertUnderline(Class<T> clazz, T entity, String tableName) {
        return DatabaseSwitch.insertUnderline("", clazz, entity, tableName);
    }

    public static int update(String sql) {
        return DatabaseSwitch.update("", sql);
    }

    public static int update(String tableName, String setField, String condition) {
        return DatabaseSwitch.update("", tableName, setField, condition);
    }

    public static int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap){
        return DatabaseSwitch.update("", tableName, paramMap, whereConditionMap);
    }

    public static int delete(String sql){
        return DatabaseSwitch.delete("", sql);
    }

    public static int delete(String tableName, String condition) {
        return DatabaseSwitch.delete("", tableName, condition);
    }

    public static int delete(String tableName, Map<String, Object> deleteMap){
        return DatabaseSwitch.delete("", tableName, deleteMap);
    }

    public static Map<String, Object> selectOne(String sql) {
        return DatabaseSwitch.selectOne("",sql);
    }


    public static Map<String, Object> selectOne(String tableName, String condition) {
       return DatabaseSwitch.selectOne("", tableName, condition);
    }

    public static <T> T selectOne(Class<T> clazz,String tableName, String condition) {
        return DatabaseSwitch.selectOne("", clazz, tableName, condition);
    }

    public static <T> T selectOne(Class<T> clazz, String sql) {
        return DatabaseSwitch.selectOne("", clazz, sql);
    }

    public static Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap) {
        return DatabaseSwitch.selectOne("", tableName, whereConditionMap);
    }


    public static <T> T selectOne(Class<T> clazz, String tableName,Map<String, Object> whereConditionMap) {
        return DatabaseSwitch.selectOne("", clazz, tableName, whereConditionMap);
    }


    public static List<Map<String, Object>> selectList(String tableName, String whereCondition, String orderByCondition) {
        return DatabaseSwitch.selectList("", tableName, whereCondition, orderByCondition);
    }

    public static List<Map<String,Object>> selectList(String sql) {
        return DatabaseSwitch.selectList("", sql);
    }


    public static List<Map<String,Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return DatabaseSwitch.selectList("", tableName, whereConditionMap, orderByCondition);
    }

    public static <T> List<T> selectList(Class<T> clazz,String sql) {
        return DatabaseSwitch.selectList("", clazz, sql);
    }

    public static <T> List<T> selectList(Class<T> clazz, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        return DatabaseSwitch.selectList("", clazz, tableName, whereConditionMap, orderByCondition);
    }

    public static Long selectCount(String sql) {
       return DatabaseSwitch.selectCount("", sql);
    }

    public static Long selectCount(String tableName, String condition) {
        return DatabaseSwitch.selectCount("", tableName, condition);
    }

    public static List<Map<String, Object>> selectColumn(String tableName, String field, String condition) {
       return DatabaseSwitch.selectColumn("", tableName, field, condition);
    }

    public static List<Map<String, Object>> selectColumn(String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                           Map<String, String> orderByCondition) {
        return DatabaseSwitch.selectColumn("",tableName, fieldList, whereConditionMap, orderByCondition);
    }
}
