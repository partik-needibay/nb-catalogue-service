package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonPOJOBuilder
public class VendorAddressDTO {
    public Integer accountId;

    @NotBlank(message = "Billing Address is required")
    private String billingAddress;

    @NotBlank(message = "Shipping Address is required")
    private String shippingAddress;
}
