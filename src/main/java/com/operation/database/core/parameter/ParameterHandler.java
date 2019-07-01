package com.operation.database.core.parameter;

import java.util.List;

/**
 * @author Qinyadong
 * @date 2019/6/29 8:12
 * @desciption 数据库操作参数处理接口
 * @since
 */
public interface ParameterHandler {

    /**
     * 获取参数值
     * @return
     */
    List<Object> getParamValue();

    /**
     * 获取预编译的sql语句
     * @return
     */
    String getPrepareSql();
}
