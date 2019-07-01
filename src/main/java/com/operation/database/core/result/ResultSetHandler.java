package com.operation.database.core.result;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 7:31
 * @desciption
 * @since
 */
public class ResultSetHandler {

    private ResultSet resultSet;

    public ResultSetHandler(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public List<Map<String, Object>> handle(List<String> columnList) throws SQLException {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (resultSet == null) {
            return mapList;
        }

        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (String column : columnList) {
                Object object = resultSet.getObject(column);
                map.put(column, object);
            }
            mapList.add(map);
        }

        return mapList;
    }

    public List<String> getInfoList(String columnLabel) throws SQLException {
        List<String> list = Lists.newArrayList();
        while (resultSet.next()) {
            list.add(resultSet.getString(columnLabel));
        }
        return list;
    }

    public Long getCount() throws SQLException {
        resultSet.next();
        return resultSet.getLong(1);
    }
    public void close() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
