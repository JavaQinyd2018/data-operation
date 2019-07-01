package com.operation.database.core.parameter;

import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 13:45
 * @desciption
 * @since
 */
public class DeleteParameterHandler extends AbstractParameterHandler {

    private String tableName;
    private Map<String, Object> whereCondition;

    public DeleteParameterHandler(String tableName, Map<String, Object> whereCondition) {
        this.tableName = tableName;
        this.whereCondition = whereCondition;
    }

    @Override
    public List<Object> getParamValue() {
        return list;
    }

    @Override
    public String getPrepareSql() {
        return String.format("DELETE FROM %s %s", tableName, buildCondition(whereCondition, null));
    }
}
