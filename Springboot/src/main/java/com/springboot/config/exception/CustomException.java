package com.springboot.config.exception;

public class CustomException extends RuntimeException {
    public CustomException(String msg) {
        super(msg);
    }
}
