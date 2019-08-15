package com.operation.database.base;

import com.operation.database.exception.DatabaseOperationException;

import java.io.IOException;
import java.sql.*;

/**
 * @author Qinyadong
 * @date 2019/6/29 17:57
 * @desciption
 * @since
 */
public class StatementService {
    private Connection connection;
    private ConnectionPool connectionPool;

    public StatementService(String env) {
        DatasourceConfig datasourceConfig = null;
        try {
            datasourceConfig = new DatasourceConfig(env);
        } catch (IOException e) {
            throw new DatabaseOperationException("初始化数据源配置失败");
        }
        ConnectionFactory connectionFactory = new ConnectionFactory(datasourceConfig.getDataSource());
        connectionPool = new ConnectionPool(connectionFactory, 5);
        try {
            connection = connectionPool.borrowObject();
        } catch (Exception e) {
            throw new DatabaseOperationException("获取数据库连接失败",e);
        }
    }

    public Statement getStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new DatabaseOperationException("创建statement失败",e);
        }
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new DatabaseOperationException("prepareStatement创建失败",e);
        }
    }

    public void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
                connectionPool.returnObject(connection);
            } catch (Exception e) {
                throw new DatabaseOperationException("statement释放失败",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                connectionPool.returnObject(connection);
            } catch (Exception e) {
                throw new DatabaseOperationException("preparedStatement释放失败");
            }
        }
    }

    public DatabaseMetaData getDatabaseMeta() {
        DatabaseMetaData databaseMetaData = null;
        try {
            databaseMetaData =  connection.getMetaData();
            connectionPool.returnObject(connection);
            return databaseMetaData;
        } catch (Exception e) {
            throw new DatabaseOperationException("获取数据表信息失败",e);
        }
    }

    public void setAutoCommit(boolean b) throws SQLException {
        connection.setAutoCommit(b);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
