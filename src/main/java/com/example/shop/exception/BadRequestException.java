package com.example.shop.exception;

//10.1
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}