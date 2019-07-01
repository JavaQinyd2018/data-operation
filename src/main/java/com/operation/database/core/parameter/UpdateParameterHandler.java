package com.operation.database.core.parameter;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 12:50
 * @desciption
 * @since
 */
public class UpdateParameterHandler extends AbstractParameterHandler implements ParameterHandler {

    private String tableName;
    private Map<String, Object> setFieldMap;
    private Map<String, Object> whereCondition;
    private List<Object> setValue = Collections.synchronizedList(new ArrayList<>());

    public UpdateParameterHandler(String tableName, Map<String, Object> setFieldMap, Map<String, Object> whereCondition) {
        this.tableName = tableName;
        this.setFieldMap = setFieldMap;
        this.whereCondition = whereCondition;
    }

    @Override
    public List<Object> getParamValue() {
        setValue.addAll(list);
        return setValue;
    }

    @Override
    public String getPrepareSql() {
        String condition = buildCondition(whereCondition, null);

        String setFileSqlSeq = "*";
        if (MapUtils.isNotEmpty(setFieldMap)) {
            List<String> setFiledList = Lists.newArrayList();
            setFieldMap.forEach((field, value) -> {
                if (value == null) {return;}
                setValue.add(value);
                setFiledList.add(field + " = ?");
            });
            setFileSqlSeq = StringUtils.join(setFiledList, ",");
        }
        return String.format("UPDATE %s SET %s %s", tableName, setFileSqlSeq, condition);
    }
}
