package com.draghici.hub.exceptions;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final int errorCode;

    public ProductException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProductException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
