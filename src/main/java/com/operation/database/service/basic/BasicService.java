package com.operation.database.service.basic;

import com.operation.database.entity.Configuration;
import com.operation.database.entity.DataSource;
import com.operation.database.service.init.InitDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.*;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/7 19:57
 * @Since:
 */
@Slf4j
public class BasicService {

    private static BasicService basicService = null;
    private static DataSource dataSource = null;
    private static final String DRIVER_NAME="com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            log.error("数据库驱动初始化失败，错误信息：{}",e);
        }catch (NullPointerException e) {
            log.error("数据库驱动不存在");
        }
    }


    private BasicService(String envFlag) {
        InitDataSource initDataSource = new InitDataSource();
        try {
            dataSource = initDataSource.getDataSource(envFlag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BasicService(Configuration configuration, String envFlag) {
        InitDataSource initDataSource = new InitDataSource(configuration);
        try {
            dataSource = initDataSource.getDataSource(envFlag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例
     * @param configuration
     * @return
     */
    public static BasicService getInstance(Configuration configuration, String envFlag) {
        if (configuration == null) {
            return new BasicService(envFlag);
        }else {
            return new BasicService(configuration, envFlag);
        }
    }

    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection() {
        Connection connection = null;
        String url = "";
        try {
            connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            url = connection.getMetaData().getURL();
        } catch (SQLException e) {
            log.error("获取数据库连接失败:{}",e);
        }
        String catalog = url.substring(0, url.indexOf("?")).substring(url.substring(0, url.indexOf("?")).lastIndexOf("/")+1).trim();
        if (!StringUtils.equalsIgnoreCase(catalog, dataSource.getSchame())) {
            throw new RuntimeException("数据库实例名和当前数据库连接实例名不一致");
        }
        return connection;
    }

    /**
     *关闭连接
     * @param connection
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接异常:{}",e);
            }
        }
    }

    /**
     * 获取预编译PreparedStatement
     * @param sql
     * @return
     */
    public PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * 获取预编译PreparedStatement
     * @param sql
     * @return
     */
    public PreparedStatement getPreparedStatement(Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * 获取statemnt
     * @param connection
     * @return
     */
    public Statement getStatement(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    /**
     * 重载
     * @return
     */
    public Statement getStatement() {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    /**
     * 关闭preparedStatement
     * @param preparedStatement
     */
    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭statement
     * @param statement
     */
    public void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
