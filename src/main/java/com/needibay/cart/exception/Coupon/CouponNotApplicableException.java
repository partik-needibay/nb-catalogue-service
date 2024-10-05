package com.needibay.cart.exception.Coupon;

public class CouponNotApplicableException extends RuntimeException {

    private String message;

    public CouponNotApplicableException() {}

    public CouponNotApplicableException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
