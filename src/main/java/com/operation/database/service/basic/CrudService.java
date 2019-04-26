package com.operation.database.service.basic;

import com.alibaba.fastjson.JSONObject;
import com.operation.database.entity.Configuration;
import com.operation.database.sql.SqlParser;
import com.operation.database.sql.SqlType;
import com.operation.database.utils.PreCheckUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/8 13:53
 * 数据库增删改查操作
 */
@Slf4j
public class CrudService implements BaseService {

    private BasicService basicService;
    private MetaDataService metaDataService;
    private String catalog;
    public CrudService(Configuration configuration, String envFlag) {
        basicService = BasicService.getInstance(configuration, envFlag);
        metaDataService = new MetaDataService(configuration,envFlag);
        catalog = metaDataService.getDatabaseBaseInfo().get("Catalog");
    }

    @Override
    public int insert(String sql) {
        log.info("sql语句是：{}",sql);
        int result = 0;
        SqlParser.checkSql(sql, SqlType.INSERT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        if (statement != null) {
            try {
                result = statement.executeUpdate(sql);
            } catch (SQLException e) {
                log.error("sql语句：{}插入操作执行失败，错误是：{}",sql,e);
            }finally {
                basicService.closeStatement(statement);
                basicService.closeConnection(connection);
            }
        }
        return result;
    }

    @Override
    public int insert(String tableName,Map<String, Object> paramMap) {
        int result = 0;
        checkTableExisted(catalog, tableName);
        Connection connection = basicService.getConnection();
        Map<String, List<String>> listMap = SqlParser.buildPreInsertSql(tableName, paramMap);
        String sql = listMap.entrySet().iterator().next().getKey();
        log.info("sql语句是：{}",sql);
        log.info("插入的值映射为：{}", JSONObject.toJSONString(paramMap));
        List<String> fieldList = listMap.entrySet().iterator().next().getValue();
        PreparedStatement preparedStatement = basicService.getPreparedStatement(connection, sql);
        Map<String, String> columnTypeMap = metaDataService.getColumnTypeMapByCatalogAndTableName(catalog, tableName);
        try {
            if (preparedStatement != null) {
                for (int i = 0; i < fieldList.size(); i++) {
                    try {
                        Object o = paramMap.get(fieldList.get(i));
                        if (o instanceof Enum) {
                            if (StringUtils.equalsIgnoreCase(columnTypeMap.get(fieldList.get(i)),"varchar")) {
                                o = ((Enum)o).name();
                            }
                        }
                        preparedStatement.setObject(i+1, o);
                    } catch (SQLException e) {
                        log.error("参数预编译失败：参数为{}，位置为{}",fieldList.get(i), i);
                    }
                }
                result = preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            log.error("sql语句：{}执行插入操作失败，错误信息为：{}",sql,e);
        }finally {
            basicService.closePreparedStatement(preparedStatement);
            basicService.closeConnection(connection);
        }

        return result;
    }

    @Override
    public int[] batchInsert(String tableName,List<Map<String, Object>> paramMapList) {
        int[] result = null;
        checkTableExisted(catalog, tableName);
        Map<String, Object> paramMap = paramMapList.get(0);
        Map<String, List<String>> listMap = SqlParser.buildPreInsertSql(tableName, paramMap);
        String sql = listMap.entrySet().iterator().next().getKey();
        log.info("sql语句是：{}",sql);
        List<String> fieldList = listMap.entrySet().iterator().next().getValue();
        Connection connection = basicService.getConnection();
        PreparedStatement preparedStatement = basicService.getPreparedStatement(connection, sql);
        try {
            connection.setAutoCommit(false);
            if (preparedStatement != null) {
                for (Map<String, Object> map : paramMapList) {
                    log.info("插入的值映射为：{}", JSONObject.toJSONString(map));
                    for (int i = 0; i < fieldList.size(); i++) {
                        try {
                            preparedStatement.setObject(i+1, map.get(fieldList.get(i)));
                        } catch (SQLException e) {
                            log.error("参数预编译失败：参数为{}，位置为{}",fieldList.get(i), i);
                        }
                    }
                    try {
                        preparedStatement.addBatch();
                    } catch (SQLException e) {
                        log.error("sql语句：{}添加批量插入操作失败，错误信息为：{}",sql,e);
                    }
                }
                try {
                    result = preparedStatement.executeBatch();
                    connection.commit();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    connection.rollback();
                    log.error("sql语句：{}执行插入操作失败，错误信息为：{}",sql,e);
                }
            }
        } catch (SQLException e) {
            log.error("批量插入失败或者回滚失败，错误信息为：{}",e);
        }finally {
            basicService.closePreparedStatement(preparedStatement);
            basicService.closeConnection(connection);
        }
        return result;
    }

    @Override
    public int update(String sql) {
        log.info("sql语句是：{}",sql);
        int result = 0;
        SqlParser.checkSql(sql, SqlType.UPDATE);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        if (statement != null) {
            try {
                result = statement.executeUpdate(sql);
            } catch (SQLException e) {
                log.error("sql语句：{}更新操作执行失败，错误是：{}",sql,e);
            }finally {
                basicService.closeStatement(statement);
                basicService.closeConnection(connection);
            }
        }
        return result;
    }

    public int update(String tableName, String setField, String condition) {
        checkTableExisted(catalog, tableName);
        if (StringUtils.isBlank(setField)) {
            throw new IllegalArgumentException("修改的字段不能为空");
        }
        if (StringUtils.isNotBlank(condition)) {
            condition = String.format("WHERE %s", condition);
        }else {
            condition = "";
        }
        String sql = String.format("UPDATE %s set %s %s", tableName, setField, condition);
        return update(sql);
    }

    @Override
    public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap) {
        int result = 0;
        checkTableExisted(catalog, tableName);
        Connection connection = basicService.getConnection();
        Map<String, List<String>> listMap = SqlParser.buildPreUpdateSql(tableName, paramMap,whereConditionMap);
        String sql = listMap.entrySet().iterator().next().getKey();
        log.info("Preparing sql语句是：{}",sql);
        List<String> fieldList = listMap.entrySet().iterator().next().getValue();
        PreparedStatement preparedStatement = basicService.getPreparedStatement(connection, sql);
        paramMap.putAll(whereConditionMap);
        log.info("插入的值映射为：{}", JSONObject.toJSONString(paramMap));
        try {
            if (preparedStatement != null) {
                for (int i = 0; i < fieldList.size(); i++) {
                    try {
                        preparedStatement.setObject(i+1, paramMap.get(fieldList.get(i)));
                    } catch (SQLException e) {
                        log.error("参数预编译失败：参数为{}，位置为{}",fieldList.get(i), i);
                    }
                }
                result = preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            log.error("sql语句：{}执行更新操作失败，错误信息为：{}",sql,e);
        }finally {
            basicService.closePreparedStatement(preparedStatement);
            basicService.closeConnection(connection);
        }
        return result;
    }

    @Override
    public int delete(String sql) {
        log.info("sql语句是：{}",sql);
        int result = 0;
        SqlParser.checkSql(sql, SqlType.DELETE);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        if (statement != null) {
            try {
                result = statement.executeUpdate(sql);
            } catch (SQLException e) {
                log.error("sql语句：{}执行删除操作失败，错误信息为：{}",sql,e);
            }finally {
                basicService.closeStatement(statement);
                basicService.closeConnection(connection);
            }
        }
        return result;
    }

    public int delete(String tableName, String condition) {
        checkTableExisted(catalog, tableName);
        if (StringUtils.isNotBlank(condition)) {
            condition = String.format("WHERE %s", condition);
        }else {
            condition = "";
        }
        String sql = String.format("DELETE FROM %s %s", tableName, condition);
        return delete(sql);
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        log.info("sql语句是：{}",sql);
        SqlParser.checkSql(sql, SqlType.SELECT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        String tableName = SqlParser.getTableNameFromSql(sql, SqlType.SELECT);
        List<String> columnList = metaDataService.getColumnListByCatalogAndTableName(catalog, tableName);
        ResultSet resultSet = null;
        try {
            if (statement != null) {
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    Map<String, Object> map = Maps.newLinkedHashMap();
                    ResultSet finalResultSet = resultSet;
                    columnList.forEach(column -> {
                        try {
                            map.put(column, finalResultSet.getObject(column));
                        } catch (SQLException e) {
                            log.error("resultSet获取参数失败，列名为：{}",column);
                        }
                    });
                    return map;
                }
            }
        } catch (SQLException e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        } finally {
            closeResultSet(resultSet);
            basicService.closeStatement(statement);
            basicService.closeConnection(connection);
        }
        return null;
    }

    public Map<String, Object> selectOne(String tableName, String condition) {
        checkTableExisted(catalog, tableName);
        if (StringUtils.isNotBlank(condition)) {
            condition = String.format("WHERE %s", condition);
        }else {
            condition = "";
        }
        String sql = String.format("SELECT * FROM %s %s", tableName, condition).trim();
        return selectOne(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap) {
        checkTableExisted(catalog, tableName);
        Connection connection = basicService.getConnection();
        Map<String, List<String>> listMap = SqlParser.buildPreSelectSql(tableName,whereConditionMap,null);
        String sql = listMap.entrySet().iterator().next().getKey();
        log.info("sql语句是：{}",sql);
        log.info("插入的值映射为：{}", JSONObject.toJSONString(whereConditionMap));
        List<String> conditionList = listMap.entrySet().iterator().next().getValue();
        List<String> columnList = metaDataService.getColumnListByCatalogAndTableName(catalog, tableName);
        PreparedStatement preparedStatement = basicService.getPreparedStatement(connection, sql);
        ResultSet resultSet = null;
        try {
            if (preparedStatement != null) {
                for (int i = 0; i < conditionList.size(); i++) {
                    try {
                        preparedStatement.setObject(i+1, whereConditionMap.get(conditionList.get(i)));
                    } catch (SQLException e) {
                        log.error("参数预编译失败：参数为{}，位置为{}",conditionList.get(i), i);
                    }
                }
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Map<String, Object> map = Maps.newLinkedHashMap();
                    ResultSet finalResultSet = resultSet;
                    columnList.forEach(column -> {
                        try {
                            map.put(column, finalResultSet.getObject(column));
                        } catch (SQLException e) {
                            log.error("resultSet获取参数失败，列名为：{}",column);
                        }
                    });
                    return map;
                }
            }
        }catch (Exception e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        }finally {
            closeResultSet(resultSet);
            basicService.closePreparedStatement(preparedStatement);
            basicService.closeConnection(connection);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        log.info("sql语句是：{}",sql);
        List<Map<String, Object>> list = Lists.newArrayList();
        SqlParser.checkSql(sql, SqlType.SELECT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        String tableName = SqlParser.getTableNameFromSql(sql, SqlType.SELECT);
        List<String> columnList = metaDataService.getColumnListByCatalogAndTableName(catalog, tableName);
        ResultSet resultSet = null;
        try {
            if (statement != null) {
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    Map<String, Object> map = Maps.newLinkedHashMap();
                    ResultSet finalResultSet = resultSet;
                    columnList.forEach(column -> {
                        try {
                            map.put(column, finalResultSet.getObject(column));
                        } catch (SQLException e) {
                            log.error("resultSet获取参数失败，列名为：{}",column);
                        }
                    });
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        } finally {
            closeResultSet(resultSet);
            basicService.closeStatement(statement);
            basicService.closeConnection(connection);
        }
        return list;
    }

    public List<Map<String, Object>> selectList(String tableName, String whereCondition, String orderByCondition) {
        checkTableExisted(catalog, tableName);
        if (StringUtils.isNotBlank(whereCondition)) {
            whereCondition = String.format("WHERE %s", whereCondition);
        }else {
            whereCondition = "";
        }

        if (StringUtils.isBlank(orderByCondition)) {
            orderByCondition = "";
        }
        String sql = String.format("SELECT * FROM %s %s %s", tableName, whereCondition, orderByCondition).trim();
        return selectList(sql);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        List<Map<String, Object>> list = Lists.newArrayList();
        checkTableExisted(catalog, tableName);
        Connection connection = basicService.getConnection();
        Map<String, List<String>> listMap = SqlParser.buildPreSelectSql(tableName,whereConditionMap,orderByCondition);
        String sql = listMap.entrySet().iterator().next().getKey();
        log.info("sql语句是：{}",sql);
        log.info("插入的值映射为：{}", JSONObject.toJSONString(whereConditionMap));
        List<String> conditionList = listMap.entrySet().iterator().next().getValue();
        List<String> columnList = metaDataService.getColumnListByCatalogAndTableName(catalog, tableName);
        PreparedStatement preparedStatement = basicService.getPreparedStatement(connection, sql);
        ResultSet resultSet = null;
        try {
            if (preparedStatement != null) {
                for (int i = 0; i < conditionList.size(); i++) {
                    try {
                        preparedStatement.setObject(i+1, whereConditionMap.get(conditionList.get(i)));
                    } catch (SQLException e) {
                        log.error("参数预编译失败：参数为{}，位置为{}",conditionList.get(i), i);
                    }
                }
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Map<String, Object> map = Maps.newLinkedHashMap();
                    ResultSet finalResultSet = resultSet;
                    columnList.forEach(column -> {
                        try {
                            map.put(column, finalResultSet.getObject(column));
                        } catch (SQLException e) {
                            log.error("resultSet获取参数失败，列名为：{}",column);
                        }
                    });
                    list.add(map);
                }
            }
        }catch (Exception e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        }finally {
            closeResultSet(resultSet);
            basicService.closePreparedStatement(preparedStatement);
            basicService.closeConnection(connection);
        }
        return list;
    }

    @Override
    public Long selectCount(String sql) {
        log.info("sql语句是：{}",sql);
        Long result = null;
        SqlParser.checkSql(sql, SqlType.SELECT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        ResultSet resultSet = null;
        try {
            if (statement != null) {
                resultSet = statement.executeQuery(sql);
                resultSet.next();
                result = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        } finally {
            closeResultSet(resultSet);
            basicService.closeStatement(statement);
            basicService.closeConnection(connection);
        }
        return result;
    }

    public Long selectCount(String tableName, String condition) {
        checkTableExisted(catalog,tableName);
        if (StringUtils.isNotBlank(condition)) {
            condition = String.format("WHERE %s", condition);
        }else {
            condition = "";
        }
        String sql = String.format("SELECT Count(*) FROM %s %s", tableName, condition);
        return selectCount(sql);
    }

    public List<Object> selectByField(String tableName, String field, String condition) {
        checkTableExisted(catalog,tableName);
        if (StringUtils.isNotBlank(condition)) {
            condition = String.format("WHERE %s", condition);
        }else {
            condition = "";
        }
        String sql = String.format("SELECT * FROM %s %s",tableName, condition);
        List<Map<String, Object>> mapList = selectList(sql);
        return mapList.stream().map(resultMap -> resultMap.get(field)).collect(Collectors.toList());
    }


    public Map<String, Object> selectByFieldList(List<String> fieldList, String sql) {
        log.info("sql语句是：{}",sql);
        Map<String, Object> map = Maps.newLinkedHashMap();
        PreCheckUtils.checkEmpty(fieldList, "查询字段集不能为空");
        SqlParser.checkSql(sql, SqlType.SELECT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        ResultSet resultSet = null;
        try {
            if (statement != null) {
                resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        ResultSet finalResultSet = resultSet;
                        fieldList.forEach(field -> {
                            try {
                                Object object = finalResultSet.getObject(field);
                                map.put(field, object);
                            } catch (SQLException e) {
                                log.error("resultSet获取参数失败，列名为：{}",field);
                            }
                        });
                }
            }
        } catch (Exception e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        } finally {
            closeResultSet(resultSet);
            basicService.closeStatement(statement);
            basicService.closeConnection(connection);
        }
        return map;
    }

    public List<Map<String, Object>> selectListByFieldList(List<String> fieldList, String sql) {
        log.info("sql语句是：{}",sql);
        List<Map<String, Object>> list = Lists.newArrayList();
        PreCheckUtils.checkEmpty(fieldList, "查询字段集不能为空");
        SqlParser.checkSql(sql, SqlType.SELECT);
        Connection connection = basicService.getConnection();
        Statement statement = basicService.getStatement(connection);
        ResultSet resultSet = null;
        try {
            if (statement != null) {
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    Map<String, Object> map = Maps.newLinkedHashMap();
                    ResultSet finalResultSet = resultSet;
                    fieldList.forEach(field -> {
                        try {
                            Object object = finalResultSet.getObject(field);
                            map.put(field, object);
                        } catch (SQLException e) {
                            log.error("resultSet获取参数失败，列名为：{}",field);
                        }
                    });
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            log.error("sql是{}执行查询操作失败，错误原因是：{}",sql, e);
        } finally {
            closeResultSet(resultSet);
            basicService.closeStatement(statement);
            basicService.closeConnection(connection);
        }
        return list;
    }

    private void checkTableExisted(String catalog, String table) {
        PreCheckUtils.checkEmpty(table, "数据库表名不能为空");
        if (!metaDataService.getTableListByCatalog(catalog).contains(table)) {
            throw new IllegalArgumentException("当前数据库配置不存在"+table+"表");
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
