package com.operation.database.core.execute;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Qinyadong
 * @date 2019/6/28 23:28
 * @desciption 可查询接口
 * @since
 */
public interface QueryExecutable {

    /**
     * 执行查询
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    ResultSet executeQuery(String sql, Object[] args) throws SQLException;
}
