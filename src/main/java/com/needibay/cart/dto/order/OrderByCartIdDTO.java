package com.needibay.cart.dto.order;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class OrderByCartIdDTO {

    @NotNull(message = "No Cart found for the User")
    private Integer cartId;

    @NotNull(message = "No Shipping Address Selected For the order!")
    private Integer shippingAddress;

    @NotNull(message = "No Billing Address Selected For the order!")
    private Integer billingAddress;

    @NotBlank(message = "Payment Method is required")
    private String paymentMethod;

    private String gstInfo;

    @NotBlank(message = "No Customer Name Found!")
    private String customerFullName;

    @NotBlank(message = "No Customer Email Found!")
    @Email(message = "Enter a valid email address")
    private String customerEmail;

    @NotBlank(message = "No Customer Phone Found!")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String customerPhone;

}
