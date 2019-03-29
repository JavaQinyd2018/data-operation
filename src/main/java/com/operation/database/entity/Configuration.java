package com.operation.database.entity;

import com.operation.database.config.DatabaseContext;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/7 17:53
 * @Since:
 */
@Data
@ToString
public class Configuration {
    /**
     * resource 下面项目的相对路径
     */
    private String configFilePath;

    public Configuration() {
        configFilePath = DatabaseContext.CONFIG_FILE;
    }

    public Configuration(String configFilePath) {
        this.configFilePath = configFilePath;
    }

}
