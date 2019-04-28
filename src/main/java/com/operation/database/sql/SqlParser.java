package com.operation.database.sql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.operation.database.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.UnknownException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:01
 * sql解析器
 */

@Slf4j
public class SqlParser {

    public static void checkSql(String sql, SqlType sqlType) {

        PreCheckUtils.checkEmpty(sql, "sql语句不能为空");
        switch (sqlType) {
            case INSERT:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "insert ")
                        && StringUtils.containsIgnoreCase(sql.trim()," values")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "insert"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,插入类型的sql语句必须以insert的开始,包含values关键字,\nsql是："+sql);
                }
                break;
            case UPDATE:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "update ")
                        && StringUtils.containsIgnoreCase(sql.trim()," set")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "update"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,修改类型的sql语句必须以update的开始,并且包含set关键字,\nsql是："+sql);
                }
                break;
            case DELETE:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "delete ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "delete"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,删除类型的sql语句必须以delete的开始,包含from关键字,\nsql是："+sql);
                }
                break;
            case SELECT:
                if (!(StringUtils.containsIgnoreCase(sql.trim(), "select ")
                        && StringUtils.containsIgnoreCase(sql.trim()," from")
                        && StringUtils.startsWithIgnoreCase(sql.trim(), "select"))) {
                    throw new IllegalArgumentException("sql语句格式不合法,查询类型的sql语句必须以select的开始,包含from关键字,\nsql是："+sql);
                }
                break;
            default:
                throw new UnknownException(new IllegalArgumentException("没有对应类型的sql"));
        }
    }

    public static String getTableNameFromSql(String sql, SqlType sqlType) {
        String tableName = "";
        if (SqlType.INSERT.equals(sqlType)) {
            if (sql.contains("insert")) {
                tableName = sql.substring(sql.indexOf("into") + "into".length(), sql.indexOf("(")).trim();
            }else {
                tableName = sql.substring(sql.indexOf("INTO") + "INTO".length(), sql.indexOf("(")).trim();
            }
        }else if (SqlType.UPDATE.equals(sqlType)) {
            if (sql.contains("update")) {
                tableName = sql.substring(sql.indexOf("update")+"update".length(), sql.indexOf("set")).trim();
            }else {
                tableName = sql.substring(sql.indexOf("UPDATE")+"UPDATE".length(), sql.indexOf("SET")).trim();
            }
        }else if (SqlType.DELETE.equals(sqlType)){
            if (sql.contains("delete") && sql.contains("where")) {
                tableName = sql.substring(sql.indexOf("from")+"from".length(),sql.indexOf("where")).trim();
            }else if (sql.contains("DELETE") && sql.contains("WHERE")){
                tableName = sql.substring(sql.indexOf("FROM")+"FROM".length(),sql.indexOf("WHERE")).trim();
            }else if (sql.contains("delete") && !sql.contains("where")) {
                tableName = sql.substring(sql.indexOf("from")+"from".length()).trim();
            }else if (sql.contains("DELETE") && !sql.contains("WHERE")) {
                tableName = sql.substring(sql.indexOf("FROM")+"FROM".length()).trim();
            }else {
                throw new IllegalArgumentException("删除sql语句格式不合法");
            }
        }else if (SqlType.SELECT.equals(sqlType)) {
            if (sql.contains("select") && sql.contains("where")) {
                tableName = sql.substring(sql.indexOf("from") + "from".length(), sql.indexOf("where")).trim();
            }else if (sql.contains("SELECT") && sql.contains("WHERE")){
                tableName = sql.substring(sql.indexOf("FROM") + "FROM".length(), sql.indexOf("WHERE")).trim();
            }else if (sql.contains("select") && !sql.contains("WHERE")) {
                tableName = sql.substring(sql.indexOf("from")+"from".length()).trim();
            }else if (sql.contains("SELECT") && !sql.contains("WHERE")) {
                tableName = sql.substring(sql.indexOf("FROM")+"FROM".length()).trim();
            }else {
                throw new IllegalArgumentException("查询sql语句格式不合法");
            }
        }
        return tableName;
    }

    public static String checkAndTransform(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("校验对象不能为空");
        }
        String value = "";
        if (object instanceof String) {
            value = String.format("'%s'",object);
        }else if (object instanceof Integer) {
            value = String.valueOf((Integer)object);
        }else if (object instanceof Long) {
            value = String.valueOf((Long)object);
        }else if (object instanceof Boolean) {
            value = String.valueOf((Boolean)object);
        }else if (object instanceof Date) {
            value = String.format("'%s'",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)object));
        }else if (object instanceof CharSequence) {
            value = String.format("'%s'",((CharSequence)object));
        }else if (object instanceof Double) {
            value = String.valueOf((Double)object);
        }else if (object instanceof Enum) {
            value = ((Enum) object).name();
        }else if (object instanceof Float) {
            value = String.valueOf((Float)object);
        }else if (object instanceof BigDecimal) {
            value = String.valueOf((BigDecimal)object);
        }else if (object instanceof Byte) {
            value = String.valueOf((Byte)object);
        }
        return value;
    }

    public static String buildInsertSql(String table, Map<String, Object> paramMap) {
        PreCheckUtils.checkEmpty(paramMap, "构建的参数不能为空");
        List<String> filedList = Lists.newArrayList();
        List<String> valueList = Lists.newArrayList();
        paramMap.forEach((key, value) -> {
            filedList.add(key);
            valueList.add(SqlParser.checkAndTransform(value));
        });

        if (CollectionUtils.isEmpty(filedList) || CollectionUtils.isEmpty(valueList)) {
            throw new RuntimeException("构建sql的字段或者值不能为空");
        }

        String keySql = StringUtils.join(filedList.toArray(), ",");
        String valueSql = StringUtils.join(valueList.toArray(), ",");
        return String.format("INSERT INTO %s (%s) VALUES (%s)",table, keySql, valueSql);
    }

    public static Map<String, List<String>> buildPreInsertSql(String table, Map<String, Object> paramMap) {
        PreCheckUtils.checkEmpty(paramMap, "构建的参数不能为空");
        List<String> filedList = Lists.newArrayList();
        List<String> valueList = Lists.newArrayList();
        paramMap.forEach((key, value) -> {
            if (value == null) {return;}
            filedList.add(key);
            valueList.add("?");
        });
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, StringUtils.join(filedList.toArray(), ","), StringUtils.join(valueList, ","));
        Map<String, List<String>> listMap = Maps.newLinkedHashMap();
        listMap.put(sql, filedList);
        return listMap;
    }

    public static String buildUpdateSql(String tableName, Map<String, Object> setFieldMap, Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(setFieldMap, "设定的字段属性不能为空");
        List<String> targetSetList = new ArrayList<>();
        setFieldMap.forEach((field, value) -> {
            targetSetList.add(String.format("%s=%s",field, checkAndTransform(value)));
        });

        List<String> whereConditionList = new ArrayList<>();
        String sqlWhereCondition = "";
        if (MapUtils.isNotEmpty(whereConditionMap)) {
            whereConditionMap.forEach((condition, value) -> {
                whereConditionList.add(String.format("%s=%s",condition, checkAndTransform(value)));
            });
            sqlWhereCondition = String.format("WHERE %s",StringUtils.join(whereConditionList.toArray(), " AND "));
        }
        return String.format("UPDATE %s SET %s %s ",tableName, StringUtils.join(targetSetList.toArray(), ", "),sqlWhereCondition);

    }

    public static String buildSelectSql(String table, List<String> fieldList, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {

        if (MapUtils.isEmpty(whereConditionMap)) {
            throw new IllegalArgumentException("where 条件不能为空");
        }
        String fieldString = "";
        if (CollectionUtils.isEmpty(fieldList)) {
            fieldString = "*";
        }else {
            fieldString = StringUtils.join(fieldList, ",");
        }
        List<String> whereConditionList = new ArrayList<>();
        whereConditionMap.forEach((filed, value) -> {
            whereConditionList.add(String.format("%s=%s",filed,checkAndTransform(value)));
        });

        final String[] orderBy = {""};
        if (MapUtils.isNotEmpty(orderByCondition)) {
            if (orderByCondition.size() != 1) {
                throw new IllegalArgumentException("排序条件只允许为1条");
            }
            orderByCondition.forEach((key, value) -> {
                if (!(StringUtils.equals(value, "desc") || StringUtils.equals(value, "asc"))) {
                    throw new IllegalArgumentException("排序方式不合法");
                }
                orderBy[0] = String.format("ORDER BY %s %s", key, value);
            });
        }
        return String.format("SELECT %s FROM %s WHERE %s %s",fieldString,  table, StringUtils.join(whereConditionList, " AND "), orderBy[0]);
    }

    public static Map<String, List<String>> buildPreUpdateSql(String tableName, Map<String, Object> setFieldMap,Map<String, Object> whereConditionMap) {
        PreCheckUtils.checkEmpty(setFieldMap, "设定的字段属性不能为空");
        List<String> fieldList = Lists.newArrayList();
        Map<String, List<String>> map = Maps.newLinkedHashMap();
        List<String> valueList = Lists.newArrayList();
        setFieldMap.forEach((field, value) -> {
            if (value == null) {return;}
            fieldList.add(String.format("%s=?",field));
            valueList.add(field);
        });
        List<String> conditionValue = Lists.newArrayList();
        String sqlWhereCondition = "";
        if (MapUtils.isNotEmpty(whereConditionMap)) {
            whereConditionMap.forEach((condition, value) -> {
                if (value == null) {return;}
                valueList.add(condition);
                conditionValue.add(String.format("%s=?",condition));
            });
            sqlWhereCondition = String.format("WHERE %s",StringUtils.join(conditionValue.toArray(), " and "));
        }
        String sql = String.format("UPDATE %s SET %s %s", tableName, StringUtils.join(fieldList.toArray(), ","), sqlWhereCondition );
        map.put(sql, valueList);
        return map;
    }

    /**
     * 构建预编译selectsql
     * @param tableName
     * @param whereConditionMap
     * @param orderByCondition
     * @return
     */
    public static Map<String, List<String>> buildPreSelectSql(String tableName, Map<String, Object> whereConditionMap,Map<String, String> orderByCondition) {
        Map<String, List<String>> map = Maps.newLinkedHashMap();
        List<String> valueList = Lists.newArrayList();
        List<String> conditionValue = Lists.newArrayList();
        String sqlWhereCondition = "";
        if (MapUtils.isNotEmpty(whereConditionMap)) {
            whereConditionMap.forEach((condition, value) -> {
                if (value == null) {return;}
                valueList.add(condition);
                conditionValue.add(String.format("%s=?",condition));
            });
            sqlWhereCondition = String.format("WHERE %s",StringUtils.join(conditionValue.toArray(), " AND "));
        }

        final String[] orderBy = {""};
        if (MapUtils.isNotEmpty(orderByCondition)) {
            if (orderByCondition.size() != 1) {
                throw new IllegalArgumentException("排序条件只允许为1条");
            }
            orderByCondition.forEach((key, value) -> {
                if (!(StringUtils.equals(value, "desc") || StringUtils.equals(value, "asc"))) {
                    throw new IllegalArgumentException("排序方式不合法");
                }
                orderBy[0] = String.format("ORDER BY %s %s", key, value);
            });
        }
        String sql = String.format("SELECT * FROM %s %s %s", tableName, sqlWhereCondition, orderBy[0]);
        map.put(sql, valueList);
        return map;
    }

}
