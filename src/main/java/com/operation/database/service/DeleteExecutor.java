package com.operation.database.service;

import com.operation.database.base.StatementService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/30 9:38
 * @desciption
 * @since
 */
class DeleteExecutor {
    private BaseDatabaseService baseDatabaseService;

    DeleteExecutor(String env) {
        StatementService service = new StatementService(env);
        baseDatabaseService = new BaseDatabaseService(service);
    }

    int delete(String sql){
        return baseDatabaseService.delete(sql);
    }

    int delete(String tableName, String condition) {
        if (StringUtils.isNotBlank(condition)) {
            condition = "WHERE " + condition;
        }
        return baseDatabaseService.delete(String.format("DELETE FROM %s %s", tableName, condition));
    }

    int delete(String tableName, Map<String, Object> deleteMap){
        return baseDatabaseService.delete(tableName, deleteMap);
    }
}
