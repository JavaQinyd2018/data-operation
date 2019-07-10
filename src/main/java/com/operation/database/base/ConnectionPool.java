package com.operation.database.base;

import com.operation.database.exception.DatabaseOperationException;
import org.apache.commons.pool2.BaseObjectPool;

import java.sql.Connection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Qinyadong
 * @date 2019/6/29 16:37
 * @desciption
 * @since
 */
public class ConnectionPool extends BaseObjectPool<Connection> {

    private ConnectionFactory connectionFactory;
    private BlockingQueue<Connection> pool;
    private int max;

    ConnectionPool(ConnectionFactory connectionFactory, int max) {
        this.max = max;
        this.connectionFactory = connectionFactory;
        pool = new ArrayBlockingQueue<>(max);
        for (int i = 0; i < max; i++) {
            try {
                pool.offer(connectionFactory.create());
            } catch (Exception e) {
                throw new DatabaseOperationException("初始化连接池失败",e);
            }
        }
    }

    @Override
    public Connection borrowObject() throws Exception {
        Connection connection = pool.take();
        if (!connectionFactory.validateObject(connectionFactory.wrap(connection))){
            invalidateObject(connection);
            connection = connectionFactory.create();
        }
        return connection;
    }

    @Override
    public void returnObject(Connection connection) throws Exception {
        if (connection != null) {
           if (!connectionFactory.validateObject(connectionFactory.wrap(connection))) {
               invalidateObject(connection);
           }else if (pool.size() < max && !pool.offer(connection)) {
               connectionFactory.destroyObject(connectionFactory.wrap(connection));
           }
        }
    }

    @Override
    public void invalidateObject(Connection connection) throws Exception {
        pool.remove(connection);
        connectionFactory.destroyObject(connectionFactory.wrap(connection));
    }

    @Override
    public void close() {
        pool.forEach(connection ->
        {
            try {
                connectionFactory.destroyObject(connectionFactory.wrap(connection));
            } catch (Exception e) {
                throw new DatabaseOperationException("销毁失败",e);
            }
        });
    }
}
