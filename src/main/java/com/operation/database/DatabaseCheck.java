package com.operation.database;

import com.alibaba.fastjson.JSONObject;
import com.operation.database.check.DBCheckHelper;
import com.operation.database.check.DBCheckWithConfigHelper;
import com.operation.database.entity.Configuration;
import com.operation.database.service.SelectHelper;
import com.operation.database.utils.CSVUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/10 18:53
 */
public class DatabaseCheck {

    private static final String MESSAGE = "结果校验失败";
    public static <T> void checkEntity(Class<T> clazz, T actual, T expect, String message) {
        DBCheckHelper.checkEquals(clazz, actual, expect, message);
    }

    public static <T> void checkEntity(Class<T> clazz, T actual, T expect) {
        DBCheckHelper.checkEquals(clazz, actual, expect, MESSAGE);
    }

    public static <T> void checkEntity(String tableName, String condition, Class<T> clazz,  T expect) {
        Map<String, Object> map = Database.selectOne(tableName, condition);
        T actual = JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
        if (actual == null) {
            throw new IllegalArgumentException("校验的数据库结果不能为空");
        }
        DBCheckHelper.checkEquals(clazz, actual, expect, MESSAGE);
    }

    public static <T> void  checkEntityCollection(Class<T> clazz, Collection<T> actualCollection, Collection<T> exceptCollection, String message) {
        DBCheckHelper.checkEquals(clazz, actualCollection, exceptCollection, message);
    }


    public static <T> void  checkEntityCollection(Class<T> clazz, Collection<T> actualCollection, Collection<T> exceptCollection) {
        DBCheckHelper.checkEquals(clazz, actualCollection, exceptCollection, MESSAGE);
    }

    /**
     * 校验查询的单个字段
     * @param tableName
     * @param condition
     * @param field
     * @param expect
     * @param message
     */
    public static void checkField(String tableName, String condition, String field, Object expect, String message) {
        List<Object> objects = SelectHelper.selectByField(tableName, field, condition);
        if (CollectionUtils.isEmpty(objects) || objects.size() != 1) {
            throw new IllegalArgumentException("校验的数据库结果不能为空且必须为一条");
        }
        DBCheckHelper.checkEquals(objects.get(0), expect, message);
    }

    /**
     * 校验查询的字段对应值得集合
     * @param tableName
     * @param condition
     * @param field
     * @param expectList
     * @param message
     */
    public static void checkListField(String tableName, String condition, String field, List<Object> expectList, String message) {
        List<Object> objects = SelectHelper.selectByField(tableName, field, condition);
        DBCheckHelper.checkEquals(objects.toArray(), expectList.toArray(), message);
    }

    /**
     * 根据查询的结果和预期的map的结果进行校验
     * @param tableName
     * @param condition
     * @param expectMap
     * @param message
     */
    public static void checkMap(String tableName, String condition, Map<String, Object> expectMap, String message) {
        Map<String, Object> map = SelectHelper.selectOne(tableName, condition);
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException("校验的数据库结果不能为空且必须为一条");
        }
        DBCheckHelper.checkEquals(map, expectMap, message);
    }

    public static void checkMap(String tableName, String condition, Map<String, Object> expectMap) {
        DatabaseCheck.checkMap(tableName, condition, expectMap, MESSAGE);
    }

    /**
     * 根据csv文件的数据，进行index获取单个结果进行校验
     * @param csvPathFile
     * @param index
     * @param message
     */
    public static void checkByCsvFile(String csvPathFile, int index, String message) {
        DBCheckHelper.checkEquals(csvPathFile, index, message);
    }

    /**
     * 将csv文件中的数据拼成sql语句，查询的结果和文件中的期望结果进行校验
     * 校验整个csv文件中的数据
     * @param csvPathFile
     * @param index
     */
    public static void checkByCsvFile(String csvPathFile, int index) {
        DBCheckHelper.checkEquals(csvPathFile, index, MESSAGE);
    }

    public static void checkListByCsvFile(String csvFilePath) {
        DBCheckHelper.checkEquals(csvFilePath, MESSAGE);
    }

    public static void checkByCsvFile(String envFlag, String csvPathFile, int index, String message) {
        DBCheckWithConfigHelper.checkEquals(null, envFlag, csvPathFile, index, message);
    }

    public static void checkListByCsvFile(String envFlag,String csvFilePath) {
        DBCheckWithConfigHelper.checkEquals(null, envFlag, csvFilePath, MESSAGE);
    }
}
