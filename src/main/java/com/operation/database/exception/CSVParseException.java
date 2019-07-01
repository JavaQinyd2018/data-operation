package com.operation.database.exception;

/**
 * @author Qinyadong
 * @date 2019/7/1 11:05
 * @desciption
 * @since
 */
public class CSVParseException extends RuntimeException {

    public CSVParseException(String errorMessage) {
        super(errorMessage);
    }

    public CSVParseException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
