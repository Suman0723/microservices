package com.example.demo.exception;

public class DependencyUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DependencyUnavailableException(String message) {
        super(message);
    }
}