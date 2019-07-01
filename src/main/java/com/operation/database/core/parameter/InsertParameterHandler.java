package com.operation.database.core.parameter;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Qinyadong
 * @date 2019/6/29 13:49
 * @desciption
 * @since
 */
public class InsertParameterHandler extends AbstractParameterHandler {

    private String tableName;
    private Map<String, Object> insertMap;
    private List<Map<String, Object>> paramMapList;
    private List<Object[]> objectList = Collections.synchronizedList(new ArrayList<>());

    public InsertParameterHandler(String tableName, Map<String, Object> insertMap) {
        this.tableName = tableName;
        this.insertMap = insertMap;
    }

    public InsertParameterHandler(String tableName, List<Map<String, Object>> paramMapList) {
        this.tableName = tableName;
        this.paramMapList = paramMapList;
    }

    @Override
    public List<Object> getParamValue() {
        return list;
    }

    public List<Object[]> getParamValueList() {
        return objectList;
    }

    @Override
    public String getPrepareSql() {
        if (MapUtils.isEmpty(insertMap)) {
            throw new IllegalArgumentException("插入的数据集合不能为空");
        }
        List<String> columnList = Lists.newArrayList();
        StringBuilder valueSqlBuilder = new StringBuilder();
        insertMap.forEach((column, value) -> {
            if (value == null) {
                return;
            }
            columnList.add(column);
            list.add(value);
            valueSqlBuilder.append("?").append(",");
        });
        String valueSqlSeq = StringUtils.substringBeforeLast(valueSqlBuilder.toString(), ",");
        return String.format("INSERT INTO %s (%s) VALUES (%s)",tableName, StringUtils.join(columnList, ","), valueSqlSeq);
    }

    public String getBatchPrepareSql() {
        if (CollectionUtils.isEmpty(paramMapList)) {
            throw new IllegalArgumentException("插入的数据集合不能为空");
        }

        List<String> columnList = Lists.newArrayList();
        AtomicReference<String> valueSqlSeq = new AtomicReference<>();
        paramMapList.forEach(map -> {
            List<Object> valueList = Lists.newArrayList();
            StringBuilder valueSqlBuilder = new StringBuilder();
            insertMap.forEach((column, value) -> {
                if (value == null) {
                    return;
                }
                columnList.add(column);
                valueList.add(value);
                valueSqlBuilder.append("?").append(",");
            });
            objectList.add(valueList.toArray());
            valueSqlSeq.set(StringUtils.substringBeforeLast(valueSqlBuilder.toString(), ","));
        });
        return String.format("INSERT INTO %s (%s) VALUES (%s)",tableName, StringUtils.join(columnList, ","), valueSqlSeq.get());
    }
}
