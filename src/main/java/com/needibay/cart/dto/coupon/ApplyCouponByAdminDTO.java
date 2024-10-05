package com.needibay.cart.dto.coupon;


import com.needibay.cart.dto.product.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ApplyCouponByAdminDTO {
    private String couponCode;
    private Double subtotal;
    private Double taxAmount;

}