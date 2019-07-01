package com.operation.database.exception;

/**
 * @author Qinyadong
 * @date 2019/7/1 11:08
 * @desciption
 * @since
 */
public class ReflectProcessException extends RuntimeException {

    public ReflectProcessException(String errorMessage) {
        super(errorMessage);
    }

    public ReflectProcessException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
