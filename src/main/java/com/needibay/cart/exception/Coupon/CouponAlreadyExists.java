package com.needibay.cart.exception.Coupon;

public class CouponAlreadyExists extends RuntimeException {

    private String message;

    public CouponAlreadyExists() {}

    public CouponAlreadyExists(String msg)
    {
        super(msg);
        this.message = msg;
    }

}
