package com.needibay.cart.exception;

public class UnprocessableEntityException extends RuntimeException {
    private String message;

    public UnprocessableEntityException() {}

    public UnprocessableEntityException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
