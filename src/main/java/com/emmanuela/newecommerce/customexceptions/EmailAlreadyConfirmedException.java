package com.emmanuela.newecommerce.customexceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException(String message) {
        super(message);
    }
}
