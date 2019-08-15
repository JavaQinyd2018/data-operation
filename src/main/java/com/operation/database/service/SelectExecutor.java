package com.operation.database.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.operation.database.base.StatementService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/30 9:48
 * @desciption
 */
class SelectExecutor {

    private BaseDatabaseService databaseService;

    SelectExecutor(String env) {
        StatementService service = new StatementService(env);
        databaseService = new BaseDatabaseService(service);
    }

        Map<String, Object> selectOne(String sql) {
            return databaseService.selectOne(sql);
        }


        Map<String, Object> selectOne(String tableName, String condition) {
            if (StringUtils.isNotBlank(condition)){
                condition = "WHERE " + condition;
            }
            String sql = String.format("SELECT * FROM %s %s", tableName, condition);
            return selectOne(sql);
        }

        <T> T selectOne(Class<T> clazz, String tableName, String condition) {
            return JSONObject.parseObject(JSONObject.toJSONString(selectOne(tableName, condition)),clazz);
        }

        <T> T selectOne(Class<T> clazz, String sql) {
            return JSONObject.parseObject(JSONObject.toJSONString(selectOne(sql)),clazz);
        }

        Map<String, Object> selectOne(String tableName, Map<String, Object> whereConditionMap) {
            return databaseService.selectOne(tableName, whereConditionMap);
        }


        <T> T selectOne(Class<T> clazz, String tableName, Map<String, Object> whereConditionMap) {
            Map<String, Object> map = selectOne(tableName, whereConditionMap);
            return JSONObject.parseObject(JSONObject.toJSONString(map),clazz);
        }


        List<Map<String, Object>> selectList(String tableName, String whereCondition, String orderByCondition) {
            if (StringUtils.isNotBlank(whereCondition)) {
                whereCondition = "WHERE " + whereCondition;
            }

            if (StringUtils.isNotBlank(orderByCondition)) {
                orderByCondition = "ORDER BY " + orderByCondition;
            }

            String sql = String.format("SELECT * FROM %s %s %s",tableName, whereCondition, orderByCondition);
            return selectList(sql);
        }

        List<Map<String,Object>> selectList(String sql) {
            return databaseService.selectList(sql);
        }


        List<Map<String,Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
            return databaseService.selectList(tableName, whereConditionMap, orderByCondition);
        }

        <T> List<T> selectList(Class<T> clazz, String sql) {
            return JSONArray.parseArray(JSONObject.toJSONString(selectList(sql)), clazz);
        }

        <T> List<T> selectList(Class<T> clazz, String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
            return JSONArray.parseArray(JSONObject.toJSONString(selectList(tableName, whereConditionMap, orderByCondition)), clazz);
        }

        Long selectCount(String sql) {
            return databaseService.selectCount(sql);
        }

        Long selectCount(String tableName, String condition) {
            if (StringUtils.isNotBlank(condition)) {
                condition = "WHERE " + condition;
            }
            String sql = String.format("SELECT COUNT(*) FROM %s %s",tableName, condition);
            return selectCount(sql);
        }

        List<Map<String, Object>> selectColumn(String tableName, String field, String condition) {
            if (StringUtils.isNotBlank(condition)) {
                condition = "WHERE " + condition;
            }
            String sql = String.format("SELECT %s FROM %s %s", field, tableName, condition);
            return databaseService.selectColumn(sql, Arrays.asList(field));
        }


        List<Map<String, Object>> selectListByFieldList(String tableName, List<String> fieldList, Map<String, Object> whereConditionMap,
                                                        Map<String, String> orderByCondition) {
            return databaseService.selectColumn(tableName, fieldList, whereConditionMap, orderByCondition);
        }
}
