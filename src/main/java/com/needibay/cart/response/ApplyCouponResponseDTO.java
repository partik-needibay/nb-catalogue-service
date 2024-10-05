package com.needibay.cart.response;

import lombok.Data;

@Data
public class ApplyCouponResponseDTO {
    private Double subtotal;
    private Double taxAmount;
    private Double discountAmount;
    private Double grandTotal;
}