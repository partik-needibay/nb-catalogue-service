package com.needibay.cart.exception.Coupon;

public class CouponExpiredException extends RuntimeException {

    private String message;

    public CouponExpiredException() {}

    public CouponExpiredException(String msg)
    {
        super(msg);
        this.message = msg;
    }
    
}
