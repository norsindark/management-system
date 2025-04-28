package com.sin.management_system.infrastructures.exceptions;


import lombok.Getter;

@Getter
public class InvalidLoginException extends RuntimeException {
    private final String field;

    public InvalidLoginException(String field, String message) {
        super(message);
        this.field = field;
    }
}