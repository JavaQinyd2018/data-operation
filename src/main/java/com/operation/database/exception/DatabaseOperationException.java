package com.operation.database.exception;

/**
 * @author Qinyadong
 * @date 2019/7/1 10:57
 * @desciption 数据库操作异常类
 * @since
 */
public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String errorMessage) {
        super(errorMessage);
    }

    public DatabaseOperationException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

}
