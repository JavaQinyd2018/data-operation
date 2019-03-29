package com.operation.database.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/2 12:59
 * 数据库返回Result结果校验转化工具类
 */
public class ResultTransformUtils {

    /**
     * 数据库查询数据对象转化并获取结果
     * @param resultSet
     * @param column
     * @param type
     * @return
     * @throws SQLException
     */
    public static Object checkAndGetResult(ResultSet resultSet, String column, String type) throws SQLException {
        Object o = null;
        if ("varchar".equalsIgnoreCase(type)) {
            o = resultSet.getString(column);
        }else if ("int".equalsIgnoreCase(type)) {
            o= resultSet.getInt(column);
        }else if ("datetime".equalsIgnoreCase(type)) {
            o = resultSet.getDate(column);
        }else if ("bigint".equalsIgnoreCase(type)) {
            o = resultSet.getLong(column);
        }else if ("tinyint".equalsIgnoreCase(type)) {
            o = resultSet.getByte(column);
        }else if ("boolean".equalsIgnoreCase(type)) {
            o = resultSet.getBoolean(column);
        }else if ("text".equalsIgnoreCase(type)) {
            o = resultSet.getString(column);
        }else if ("char".equalsIgnoreCase(type)) {
            o = resultSet.getCharacterStream(column);
        }else if ("double".equalsIgnoreCase(type)) {
            o = resultSet.getDouble(column);
        }
        return o;
    }

}
