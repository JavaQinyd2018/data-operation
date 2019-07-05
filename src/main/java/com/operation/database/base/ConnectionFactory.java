package com.operation.database.base;

import com.operation.database.entity.DataSource;
import com.operation.database.exception.DatabaseOperationException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Qinyadong
 * @date 2019/6/29 16:34
 * @desciption
 * @since
 */
public class ConnectionFactory extends BasePooledObjectFactory<Connection> {

    private static final String DRIVER_NAME="com.mysql.cj.jdbc.Driver";
    private DataSource dataSource;

    ConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    static {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("数据库驱动初始化失败",e);
        }
    }

    @Override
    public Connection create() throws Exception {
        return DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<>(connection);
    }


    @Override
    public void destroyObject(PooledObject<Connection> pooledObject) throws Exception {
        Connection connection = pooledObject.getObject();
        connection.close();
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        Connection connection = p.getObject();
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
