package com.operation.database.core.execute;


import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/6/28 22:51
 * @desciption
 * @since
 */
public class PrepareStatementExecutor implements QueryExecutable, UpdateExecutable {
    private static Logger log = LoggerFactory.getLogger(PrepareStatementExecutor.class);
    private PreparedStatement preparedStatement;

    public PrepareStatementExecutor(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public ResultSet executeQuery(String sql, Object[] args) throws SQLException {
        log.debug("执行的sql语句为：{}",sql);
        if (ArrayUtils.isNotEmpty(args)) {
            log.debug("对应的参数值为：{}", JSON.toJSONString(args));
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }
        }
        return preparedStatement.executeQuery();
    }

    @Override
    public Integer executeUpdate(String sql, Object[] args) throws SQLException {
        log.debug("执行的sql语句为：{}",sql);
        if (ArrayUtils.isNotEmpty(args)) {
            log.debug("对应的参数值为：{}", JSON.toJSONString(args));
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }
        }
        return preparedStatement.executeUpdate();
    }

    public int[] executeBatch(String sql, List<Object[]> list) throws SQLException {
        log.debug("执行的sql语句为：{}",sql);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object[] objects : list) {
                for (int i = 0; i < objects.length; i++) {
                    preparedStatement.setObject(i+1,objects[i]);
                }
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        }
        return new int[]{0};
    }

}
