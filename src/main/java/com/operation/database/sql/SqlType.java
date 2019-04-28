package com.operation.database.sql;

import lombok.Getter;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 17:02
 * @Since:
 */
@Getter
public enum  SqlType {
    /**
     * 插入
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 查询
     */
    SELECT;

}
