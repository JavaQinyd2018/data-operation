package com.operation.database.core.parameter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 9:00
 * @desciption
 * @since
 */
public class SelectParameterHandler extends AbstractParameterHandler implements ParameterHandler {

    private List<String> columnList;
    private String tableName;
    private Map<String, Object> whereCondition;
    private Map<String, String> orderCondition;

    public SelectParameterHandler(List<String> columnList, String tableName, Map<String, Object> whereCondition, Map<String, String> orderCondition) {
        this.columnList = columnList;
        this.tableName = tableName;
        this.whereCondition = whereCondition;
        this.orderCondition = orderCondition;
    }

    @Override
    public List<Object> getParamValue() {
        return list;
    }

    @Override
    public String getPrepareSql() {

        String columnSqlSeq = "*";
        if (CollectionUtils.isNotEmpty(columnList)) {
            columnSqlSeq = StringUtils.join(columnList, ",");
        }

        return String.format("SELECT %s FROM %s %s",columnSqlSeq, tableName, buildCondition(whereCondition, orderCondition));
    }

}
