package com.operation.database.service;

import com.operation.database.base.StatementService;
import com.operation.database.core.execute.PrepareStatementExecutor;
import com.operation.database.core.execute.StatementExecutor;
import com.operation.database.core.parameter.DeleteParameterHandler;
import com.operation.database.core.parameter.InsertParameterHandler;
import com.operation.database.core.parameter.SelectParameterHandler;
import com.operation.database.core.parameter.UpdateParameterHandler;
import com.operation.database.core.result.MetaDataHandler;
import com.operation.database.core.result.ResultSetHandler;
import com.operation.database.entity.SqlType;
import com.operation.database.exception.DatabaseOperationException;
import com.operation.database.utils.SqlParser;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/29 19:11
 * @desciption
 * @since
 */
public class BaseDatabaseService implements BaseDatabase {

    private StatementService statementService;
    BaseDatabaseService(StatementService statementService) {
       this.statementService = statementService;
    }

    @Override
    public int insert(String sql) {
        SqlParser.checkSql(sql, SqlType.INSERT);
        return executeUpdate(sql);
    }

    @Override
    public int insert(String tableName, Map<String, Object> paramMap) {
        InsertParameterHandler insertHandler = new InsertParameterHandler(tableName, paramMap);
        String prepareSql = insertHandler.getPrepareSql();
        List<Object> paramValue = insertHandler.getParamValue();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        try {
            PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
            return executor.executeUpdate(prepareSql, paramValue.toArray());
        } catch (SQLException e) {
            throw new DatabaseOperationException("数据插入失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public int[] batchInsert(String tableName, List<Map<String, Object>> paramMapList) {
        int[] result = null;
        InsertParameterHandler insertHandler = new InsertParameterHandler(tableName, paramMapList);
        String prepareSql = insertHandler.getBatchPrepareSql();
        List<Object[]> paramValue = insertHandler.getParamValueList();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            statementService.setAutoCommit(false);
            result =  executor.executeBatch(prepareSql, paramValue);
            statementService.commit();
            statementService.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                statementService.rollback();
            } catch (SQLException e1) {
                throw new DatabaseOperationException("数据回滚失败");
            }
            throw new DatabaseOperationException("批量数据插入失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int update(String sql) {
        SqlParser.checkSql(sql, SqlType.UPDATE);
        return executeUpdate(sql);
    }

    @Override
    public int update(String tableName, Map<String, Object> paramMap, Map<String, Object> whereConditionMap) {
        UpdateParameterHandler handler = new UpdateParameterHandler(tableName, paramMap, whereConditionMap);
        String prepareSql = handler.getPrepareSql();
        List<Object> paramValue = handler.getParamValue();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            return executor.executeUpdate(prepareSql, paramValue.toArray());
        } catch (SQLException e) {
            throw new DatabaseOperationException("数据更新失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public int delete(String sql) {
        SqlParser.checkSql(sql, SqlType.DELETE);
        return executeUpdate(sql);
    }

    public int delete(String tableName, Map<String, Object> deleteMap) {
        DeleteParameterHandler handler = new DeleteParameterHandler(tableName, deleteMap);
        List<Object> paramValue = handler.getParamValue();
        String prepareSql = handler.getPrepareSql();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            return executor.executeUpdate(prepareSql, paramValue.toArray());
        } catch (SQLException e) {
            throw new DatabaseOperationException("数据删除失败",e);
        }
    }

    @Override
    public Map<String, Object> selectOne(String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        Statement statement = statementService.getStatement();
        StatementExecutor executor = new StatementExecutor(statement);

        try {
            ResultSet resultSet = executor.executeQuery(sql, null);
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            MetaDataHandler handler = new MetaDataHandler(statementService);
            String tableName = SqlParser.getTableNameFromSql(sql, SqlType.SELECT);
            List<String> columnList = handler.getColumnList(null, tableName);
            List<Map<String, Object>> result = resultSetHandler.handle(columnList);
            if (CollectionUtils.isNotEmpty(result) && result.size() != 1) {
                throw new DatabaseOperationException("查询的结果信息不止一条");
            }
            resultSetHandler.close();
            return CollectionUtils.isNotEmpty(result) ? result.get(0) : new HashMap<>();
        } catch (SQLException e) {
            throw new DatabaseOperationException("查询数据失败",e);
        }finally {
            statementService.closeStatement(statement);
        }
    }

    @Override
    public Map<String, Object> selectOne(String tableName, Map<String, Object> whereConditionMap) {
        SelectParameterHandler parameterHandler = new SelectParameterHandler(null, tableName, whereConditionMap, null);
        List<Object> paramValue = parameterHandler.getParamValue();
        String prepareSql = parameterHandler.getPrepareSql();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            MetaDataHandler handler = new MetaDataHandler(statementService);
            List<String> columnList = handler.getColumnList(null, tableName);
            ResultSet resultSet = executor.executeQuery(prepareSql, paramValue.toArray());
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            List<Map<String, Object>> result = resultSetHandler.handle(columnList);
            if (CollectionUtils.isNotEmpty(result) && result.size() != 1) {
                throw new DatabaseOperationException("查询的结果信息不止一条");
            }
            resultSetHandler.close();
            return CollectionUtils.isNotEmpty(result) ? result.get(0) : new HashMap<>();
        } catch (SQLException e) {
            throw new DatabaseOperationException("执行查询失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public List<Map<String, Object>> selectList(String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        Statement statement = statementService.getStatement();
        StatementExecutor executor = new StatementExecutor(statement);
        try {
            ResultSet resultSet = executor.executeQuery(sql, null);
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            MetaDataHandler handler = new MetaDataHandler(statementService);
            String tableName = SqlParser.getTableNameFromSql(sql, SqlType.SELECT);
            List<String> columnList = handler.getColumnList(null, tableName);
            List<Map<String, Object>> result = resultSetHandler.handle(columnList);
            resultSetHandler.close();
            return result;
        } catch (SQLException e) {
            throw new DatabaseOperationException("查询数据失败",e);
        }finally {
            statementService.closeStatement(statement);
        }
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        SelectParameterHandler parameterHandler = new SelectParameterHandler(null, tableName, whereConditionMap, orderByCondition);
        List<Object> paramValue = parameterHandler.getParamValue();
        String prepareSql = parameterHandler.getPrepareSql();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            MetaDataHandler handler = new MetaDataHandler(statementService);
            List<String> columnList = handler.getColumnList(null, tableName);
            ResultSet resultSet = executor.executeQuery(prepareSql, paramValue.toArray());
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            List<Map<String, Object>> result = resultSetHandler.handle(columnList);
            resultSetHandler.close();
            return result;
        } catch (SQLException e) {
            throw new DatabaseOperationException("执行查询失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public Long selectCount(String sql) {
        SqlParser.checkSql(sql, SqlType.SELECT);
        Statement statement = statementService.getStatement();
        StatementExecutor executor = new StatementExecutor(statement);
        try {
            ResultSet resultSet = executor.executeQuery(sql, null);
            ResultSetHandler handler = new ResultSetHandler(resultSet);
            Long count = handler.getCount();
            handler.close();
            return count;
        } catch (SQLException e) {
            throw new DatabaseOperationException("查询数据失败",e);
        }finally {
            statementService.closeStatement(statement);
        }
    }

    public Long selectCount(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        SelectParameterHandler parameterHandler = new SelectParameterHandler(null, tableName, whereConditionMap, orderByCondition);
        List<Object> paramValue = parameterHandler.getParamValue();
        String prepareSql = parameterHandler.getPrepareSql();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {
            ResultSet resultSet = executor.executeQuery(prepareSql, paramValue.toArray());
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            Long count = resultSetHandler.getCount();
            resultSetHandler.close();
            return count;
        } catch (SQLException e) {
            throw new DatabaseOperationException("执行查询失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }

    public List<Map<String, Object>> selectColumn(String tableName, List<String> columnList, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition) {
        SelectParameterHandler parameterHandler = new SelectParameterHandler(columnList, tableName, whereConditionMap, orderByCondition);
        List<Object> paramValue = parameterHandler.getParamValue();
        String prepareSql = parameterHandler.getPrepareSql();
        PreparedStatement preparedStatement = statementService.getPreparedStatement(prepareSql);
        PrepareStatementExecutor executor = new PrepareStatementExecutor(preparedStatement);
        try {

            ResultSet resultSet = executor.executeQuery(prepareSql, paramValue.toArray());
            ResultSetHandler resultSetHandler = new ResultSetHandler(resultSet);
            List<Map<String, Object>> result = resultSetHandler.handle(columnList);
            resultSetHandler.close();
            return result;
        } catch (SQLException e) {
            throw new DatabaseOperationException("执行查询失败",e);
        }finally {
            statementService.closePreparedStatement(preparedStatement);
        }
    }


    public List<Map<String, Object>> selectColumn(String sql, List<String> columnList) {
        Statement statement = statementService.getStatement();
        StatementExecutor executor = new StatementExecutor(statement);
        try {
            ResultSet resultSet = executor.executeQuery(sql, null);
            ResultSetHandler handler = new ResultSetHandler(resultSet);
            List<Map<String, Object>> result = handler.handle(columnList);
            handler.close();
            return result;
        } catch (SQLException e) {
            throw new DatabaseOperationException("数据插叙失败",e);
        }finally {
            statementService.closeStatement(statement);
        }
    }

    private Integer executeUpdate(String sql) {
        Statement statement = statementService.getStatement();
        try {
            StatementExecutor statementExecutor = new StatementExecutor(statement);
            return statementExecutor.executeUpdate(sql,null);
        } catch (SQLException e) {
            throw new DatabaseOperationException("数据操作插入、更新、删除失败",e);
        } finally {
            statementService.closeStatement(statement);
        }
    }
}
