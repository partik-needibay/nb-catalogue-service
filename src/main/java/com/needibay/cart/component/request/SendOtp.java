package com.needibay.cart.component.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendOtp {

    private String email;

    private String otp;

    public SendOtp(){}
}
