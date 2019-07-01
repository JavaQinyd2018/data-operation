package com.operation.database.core.parameter;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 12:54
 * @desciption
 * @since
 */
public abstract class AbstractParameterHandler implements ParameterHandler {
    protected List<Object> list = ListUtils.synchronizedList(new ArrayList<>());

    protected String buildCondition(Map<String, Object> conditionMap, Map<String, String> orderMap) {
        String whereSqlSeq = "";

        if (MapUtils.isNotEmpty(conditionMap)) {
            List<String> valueSqlSeq = Lists.newArrayList();
            conditionMap.forEach((field, value) -> {
                if (value == null) {
                    return;
                }
                list.add(value);
                valueSqlSeq.add(String.format("%s = ?", field));
            });

            whereSqlSeq = "WHERE " + StringUtils.join(valueSqlSeq, " AND ");
        }


        String orderSqlSeq = "";
        if (MapUtils.isNotEmpty(orderMap)) {
            List<String> orderSqlList = Lists.newArrayList();
            orderMap.forEach((field, order) -> orderSqlList.add(String.format("%s %s",field, order)));
            orderSqlSeq = "ORDER BY " + StringUtils.join(orderSqlList, ",");
        }

        return (whereSqlSeq + " " + orderSqlSeq).trim();
    }
}
