package com.operation.database.utils;

import com.operation.database.entity.SqlType;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:01
 * sql解析器
 */

public final class SqlParser {

    private SqlParser() {}

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
                throw new IllegalArgumentException("没有对应类型的sql");
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

}
