package com.needibay.cart.dto.order;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.needibay.cart.entity.Product;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OrderBy;
import java.util.List;

@Data
@JsonPOJOBuilder
public class OrderBySystem {

    private Integer customerId;
    private String customerEmail;
    private String customerPhone;
    private String customerFullName;
    private Integer incrementId;
    private String items;
    private String checkoutMethod;
    private String paymentMethod;
    private Integer shippingAddress;
    private Integer billingAddress;
    private Double grandTotal;
    private Double subtotal;
    private Double taxAmount;
    private Double orderAmount;
    private Boolean isCouponApplied;
    private String couponCode;
    private List<OrderItemDTO> orderItems;

    @Data
    public static class OrderItemDTO {
        private Integer productId;
        private String sku;
        private Double price;
        private Integer qty;
        private Double rowTotal;
        private Double taxPercent;
        private Double rowTotalTaxAmount;
        private Double taxAmount;
        private Double priceInclTax;
    }
}