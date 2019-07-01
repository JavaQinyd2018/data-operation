package com.operation.database.base;

import com.operation.database.entity.DataSource;
import com.operation.database.exception.DatabaseOperationException;
import com.operation.database.utils.PreCheckUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Qinyadong
 * @date 2019/6/29 18:01
 * @desciption
 * @since
 */
class DatasourceConfig {

    private static final String DB_CONFIG = "config/db.properties";

    private DataSource dataSource;

    DatasourceConfig(String env) throws IOException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DB_CONFIG);
        if (resourceAsStream == null) {
            throw new DatabaseOperationException("数据库配置文件不存在");
        }

        Properties properties = new Properties();
        properties.load(resourceAsStream);
        env = StringUtils.isBlank(env) ? "" : env + ".";
        String url = properties.getProperty(String.format("%sjdbc.datasource.%s", env,"url"));
        String username = properties.getProperty(String.format("%sjdbc.datasource.%s", env,"username"));
        String password = properties.getProperty(String.format("%sjdbc.datasource.%s", env,"password"));
        String schema = properties.getProperty(String.format("%sjdbc.datasource.%s", env,"schema"));
        PreCheckUtils.checkEmpty(url, "数据库URL不能为空或者不存在");
        PreCheckUtils.checkEmpty(username, "数据库用户名不能为空或者不存在");
        PreCheckUtils.checkEmpty(password, "数据库密码不能为空或者不存在");
        PreCheckUtils.checkEmpty(schema, "数据库实例不能为空或者不存在");
        dataSource = new DataSource(url, username, password, schema);
    }

    DataSource getDataSource() {
        return this.dataSource;
    }
}
