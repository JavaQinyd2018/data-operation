package com.operation.database.core.execute;

import java.sql.SQLException;

/**
 * @author Qinyadong
 * @date 2019/6/28 23:29
 * @desciption 可更新接口
 * @since
 */
public interface UpdateExecutable {

    /**
     * 执行增删改
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    Integer executeUpdate(String sql, Object[] args) throws SQLException;
}
