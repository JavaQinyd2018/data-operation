package com.operation.database.core.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Qinyadong
 * @date 2019/6/28 18:44
 * @desciption
 * @since
 */
public class StatementExecutor implements QueryExecutable, UpdateExecutable {

    private static Logger log = LoggerFactory.getLogger(StatementExecutor.class);
    private Statement statement;

    public StatementExecutor(Statement statement) {
        this.statement = statement;
    }

    @Override
    public ResultSet executeQuery(String sql, Object[] args) throws SQLException {
        log.debug("执行的sql语句为：{}",sql);
        return statement.executeQuery(sql);
    }

    @Override
    public Integer executeUpdate(String sql, Object[] args) throws SQLException {
        log.debug("执行的sql语句为：{}",sql);
        return statement.executeUpdate(sql);
    }
}
