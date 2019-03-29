package com.operation.database.service.basic;

import com.operation.database.entity.Configuration;
import com.operation.database.utils.PreCheckUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 15:08
 * @Since:
 */
@Slf4j
public class MetaDataService {

    private DatabaseMetaData metaData;

    public MetaDataService(Configuration configuration,String envFlag) {
        Connection connection = BasicService.getInstance(configuration,envFlag).getConnection();
        try {
            if (connection != null) {
                metaData = connection.getMetaData();
            }
        } catch (SQLException e) {
            log.error("获取数据库元数据失败");
            e.printStackTrace();
        }
    }

    /**
     * 打印数据库的基本信息
     */
    public void printBaseInfo() {
        log.info("=================打印数据库的基本信息==============");
        try {
            if (metaData != null) {
                log.info("当前数据库是：{}" ,metaData.getDatabaseProductName());
                log.info("当前数据库版本：{}" , metaData.getDatabaseProductVersion());
                log.info("当前数据库驱动：{}" , metaData.getDriverVersion());
                log.info("当前数据库URL：{}" , metaData.getURL());
                log.info("当前数据库是否是只读模式？：{}" , metaData.isReadOnly());
                log.info("当前数据库是否支持批量更新？：{}" , metaData.supportsBatchUpdates());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库Catalog列表
     * @return
     */
    public List<String> getCatalogList() {
        List<String> catalogList = Lists.newArrayList();
        try {
            if (metaData != null) {
                ResultSet catalogs = metaData.getCatalogs();
                while (catalogs.next()) {
                    catalogList.add(catalogs.getString("TABLE_CAT"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catalogList;
    }

    /**
     * 通过catalog获取所有的数据表
     * @param catalog
     * @return
     */
    public List<String> getTableListByCatalog(String catalog) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isBlank(catalog)) {
            throw  new IllegalArgumentException("数据库实例名不能为空");
        }
        try {
            if (metaData != null) {
                ResultSet tables = metaData.getTables(catalog, "%", null, new String[]{"TABLE"});
                while (tables.next()) {
                    list.add(tables.getString("TABLE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 根据catalog和表名获取所有的列的list
     * @param catalog
     * @param tableName
     * @return
     */
    public List<String> getColumnListByCatalogAndTableName(String catalog, String tableName) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isBlank(catalog) || StringUtils.isBlank(tableName)) {
            throw  new IllegalArgumentException("数据库实例名或者表名不能为空");
        }
        try {
            if (metaData != null) {
                ResultSet columns = metaData.getColumns(catalog, null, tableName, null);
                while (columns.next()) {
                    list.add(columns.getString("COLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据catalog和表名获取所有的列和类型映射的map
     * @param catalog
     * @param tableName
     * @return
     */
    public Map<String, String> getColumnTypeMapByCatalogAndTableName(String catalog, String tableName) {
        Map<String, String> map = Maps.newLinkedHashMap();
        PreCheckUtils.checkEmpty(catalog, "数据库实例名不能为空");
        PreCheckUtils.checkEmpty(tableName, "数据库表名不能为空");
        try {
            if (metaData != null) {
                ResultSet columns = metaData.getColumns(catalog, null, tableName, null);
                while (columns.next()) {
                    String key = columns.getString("COLUMN_NAME");
                    String value = columns.getString("TYPE_NAME");
                    map.put(key,value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取类型集合
     * @param catalog
     * @param tableName
     * @return
     */
    public List<String> getColumnTypeByCatalogAndTableName(String catalog, String tableName) {
        List<String> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(catalog, "数据库实例名不能为空");
        PreCheckUtils.checkEmpty(tableName, "数据库表名不能为空");
        try {
            if (metaData != null) {
                ResultSet columns = metaData.getColumns(catalog, null, tableName, null);
                while (columns.next()) {
                    list.add(columns.getString("TYPE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取所有的schema的list
     * @return
     */
    public List<String> getSchemaList() {
        //TABLE_SCHEM
        List<String> list = Lists.newArrayList();
        try {
            if (metaData != null) {
                ResultSet catalogs = metaData.getSchemas();
                while (catalogs.next()) {
                    list.add(catalogs.getString("TABLE_SCHEM"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取数据库的基本信息
     * @return
     */
    public Map<String, String> getDatabaseBaseInfo() {
        Map<String, String> map = Maps.newHashMap();
        try {
            map.put("DBName",metaData.getDatabaseProductName());
            map.put("DBVserion",metaData.getDatabaseProductVersion());
            map.put("DriverVserion",metaData.getDriverVersion());
            String url = metaData.getURL();
            map.put("DBURL", url);
            String catalog = url.substring(0, url.indexOf("?")).substring(url.substring(0, url.indexOf("?")).lastIndexOf("/")+1).trim();
            map.put("Catalog",catalog);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
