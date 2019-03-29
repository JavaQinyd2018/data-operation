package com.operation.database.service.init;

import com.operation.database.entity.Configuration;
import com.operation.database.entity.DataSource;
import com.operation.database.utils.PreCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 11:18
 * @Since:
 * 初始化数据源
 */
@Slf4j
public class InitDataSource {

    private Configuration configuration;

    public InitDataSource(Configuration configuration) {
        this.configuration = configuration;
    }

    public InitDataSource() {
        this.configuration = new Configuration();
    }

    /**
     * 获取数据源
     * @param envFlag
     * @return
     * @throws IOException
     */
    public DataSource getDataSource(String envFlag) throws IOException {
        String filePath = checkAndReturnConfigFilePath();
        if (filePath.endsWith(".properties")) {
            return parsePropertyFile(filePath,envFlag);
        }else if (filePath.endsWith(".conf")) {
            return parseConfigFile(filePath,envFlag);
        }
        return null;
    }

    /**
     * 校验获取配置中的文件路径
     * @return
     */
    private String checkAndReturnConfigFilePath() {

        if (StringUtils.isBlank(configuration.getConfigFilePath())) {
            throw new IllegalArgumentException("配置文件路径不存在");
        }

        if (!StringUtils.endsWith(configuration.getConfigFilePath(), ".properties")
        && !StringUtils.endsWith(configuration.getConfigFilePath(), ".conf")) {
            throw new IllegalArgumentException("配置文件不是properties文件");
        }
        return configuration.getConfigFilePath();
    }

    /**
     * 解析properties文件
     * @param filePath
     * @param envFlag
     * @return
     * @throws IOException
     */
    private DataSource parsePropertyFile(String filePath,String envFlag) throws IOException {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (resourceAsStream == null) {
            throw new RuntimeException("数据库配置信息输出流不存在");
        }
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        envFlag = StringUtils.isBlank(envFlag) ? "" : envFlag;
        String url = properties.getProperty(String.format("jdbc.%s%s", envFlag,"url"));
        String username = properties.getProperty(String.format("jdbc.%s%s", envFlag,"username"));
        String password = properties.getProperty(String.format("jdbc.%s%s", envFlag,"password"));
        String schema = properties.getProperty(String.format("jdbc.%s%s", envFlag,"schema"));
        PreCheckUtils.checkEmpty(url, "数据库URL不能为空或者不存在");
        PreCheckUtils.checkEmpty(username, "数据库用户名不能为空或者不存在");
        PreCheckUtils.checkEmpty(password, "数据库密码不能为空或者不存在");
        PreCheckUtils.checkEmpty(schema, "数据库实例不能为空或者不存在");
        return new DataSource(url, username, password,schema);
    }

    /**
     * 可以匹配所有的ext_db_url, 0-9配置
     * @param filePath
     * @return
     */
    private DataSource parseFile(String filePath) {
        DataSource dataSource = new DataSource();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (resourceAsStream == null) {
            throw new RuntimeException("配置文件不存在");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try {
            Properties properties = new Properties();
            properties.load(bufferedReader);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String property = (String)enumeration.nextElement();
                if (Pattern.matches("ext[0-9]_db_url", property)) {
                   dataSource.setUrl(properties.getProperty(property));
                }else if (Pattern.matches("ext[0-9]_db_username", property)) {
                    dataSource.setUsername(properties.getProperty(property));
                }else if (Pattern.matches("ext[0-9]_db_password", property)) {
                    dataSource.setPassword(properties.getProperty(property));
                }else if (Pattern.matches("username是："+"ext[0-9]_db_schema", property)) {
                    dataSource.setSchame(properties.getProperty(property));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * 捷信配置文件，根据环境标识。默认的是ext1_db_url配置
     * @param filePath
     * @param envFlag
     * @return
     */
    private DataSource parseConfigFile(String filePath, String envFlag) {
        DataSource dataSource = null;
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader bufferedReader = null;
        if (resourceAsStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        }else {
            String path = String.format("%s\\src\\main\\resources\\%s", System.getProperty("user.dir"), filePath.replace("/", "\\"));
            try {
                bufferedReader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                log.error("配置文件不存在",e);
            }
        }

        if (bufferedReader == null) {
            throw new RuntimeException("配置文件不存在");
        }

        try {
            Properties properties = new Properties();
            properties.load(bufferedReader);
            if (StringUtils.isBlank(envFlag)) {
                envFlag = "1";
            }
            String url = (String) properties.get(String.format("ext%s_db_url", envFlag));
            String username = (String) properties.get(String.format("ext%s_db_username", envFlag));
            String password = (String) properties.get(String.format("ext%s_db_password", envFlag));
            String schema = (String) properties.get(String.format("ext%s_db_schema", envFlag));
            PreCheckUtils.checkEmpty(url, "数据库URL不能为空或者不存在");
            PreCheckUtils.checkEmpty(username, "数据库用户名不能为空或者不存在");
            PreCheckUtils.checkEmpty(password, "数据库密码不能为空或者不存在");
            PreCheckUtils.checkEmpty(schema, "数据库实例不能为空或者不存在");
            dataSource = new DataSource(url, username, password, schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
