package com.hanu.ims.exception;

public class DbException extends RuntimeException {
    private static final String MESSAGE = "A database exception occurred.";

    /**
     * Construct a runtime DbException with nested cause
     * @param throwable
     */
    public DbException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
