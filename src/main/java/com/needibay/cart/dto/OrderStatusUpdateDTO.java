package com.needibay.cart.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    public Integer status;
    public String comment = null;
}
