package com.impaq.training.spring.webfluxexamples.ex10functionalendpoints;

import lombok.Getter;

public class IdentifierValidationException extends RuntimeException implements StatusCode{

    @Getter
    private final int statusCode;

    public IdentifierValidationException(int code, String message) {
        super(message);
        this.statusCode = code;
    }
}
