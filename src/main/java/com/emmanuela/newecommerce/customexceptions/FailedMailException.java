package com.emmanuela.newecommerce.customexceptions;

public class FailedMailException extends RuntimeException{
    public FailedMailException(String message) {
        super(message);
    }
}
