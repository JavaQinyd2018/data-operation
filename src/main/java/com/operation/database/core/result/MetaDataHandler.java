package com.operation.database.core.result;

import com.operation.database.base.StatementService;
import com.operation.database.exception.DatabaseOperationException;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/6/29 22:17
 * @desciption
 * @since
 */
public class MetaDataHandler {
    private DatabaseMetaData databaseMetaData;

    public MetaDataHandler(StatementService statementService) {
        databaseMetaData = statementService.getDatabaseMeta();
    }

    public List<String> getSchemeList() {
        try {
            ResultSet resultSet = databaseMetaData.getCatalogs();
            ResultSetHandler handler = new ResultSetHandler(resultSet);
            return handler.getInfoList("TABLE_CAT");
        } catch (SQLException e) {
            throw new DatabaseOperationException("获取数据库实例名称失败",e);
        }
    }

    public List<String> getColumnList(String catalog, String tableName) throws SQLException {
        ResultSet resultSet = databaseMetaData.getColumns(catalog, null, tableName, null);
        ResultSetHandler handler = new ResultSetHandler(resultSet);
        return handler.getInfoList("COLUMN_NAME");
    }
}
