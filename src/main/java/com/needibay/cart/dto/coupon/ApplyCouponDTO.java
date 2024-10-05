package com.needibay.cart.dto.coupon;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class ApplyCouponDTO {

    private Long cartId;

    @NotBlank(message = "Coupon Code is required")
    private String CouponCode;
}
