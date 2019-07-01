package com.operation.database.service;

import com.operation.database.base.StatementService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/30 9:27
 * @desciption
 * @since
 */
class UpdateExecutor {
    private BaseDatabaseService databaseService;

    UpdateExecutor(String env) {
        StatementService statementService = new StatementService(env);
        databaseService = new BaseDatabaseService(statementService);
    }

    int update(String sql) {
        return databaseService.update(sql);
    }

    int update(String tableName, String setField, String condition) {
        if (StringUtils.isBlank(setField)) {
            setField = "*";
        }

        if (StringUtils.isNotBlank(condition)) {
            condition = "WHERE " + condition;
        }

        String sql = String.format("UPDATE %s SET %s %s",tableName, setField, condition);
        return databaseService.update(sql);
    }

    int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap){
        return databaseService.update(tableName, paramMap, whereConditionMap);
    }
}
