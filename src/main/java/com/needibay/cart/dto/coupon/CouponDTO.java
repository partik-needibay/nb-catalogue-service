package com.needibay.cart.dto.coupon;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@JsonPOJOBuilder
public class CouponDTO {

    @NotBlank(message = "Coupon Code is required")
    private String couponCode;

    @NotBlank(message = "Coupon Type is required")
    private String couponType;

    private Double amount;

    @NotNull(message = "Percent is required")
    @Min(value = 0, message = "Percent must be positive")
    @Max(value = 100, message = "Percent cannot be more than 100")
    private Double percent;

    @NotNull(message = "Minimum Order Price is required")
    @Min(value = 0, message = "Minimum Order Price must be positive")
    private Double minOrderPrice;

    private Integer productId;

    @NotNull(message = "Maximum Discount Amount is required")
    @Min(value = 0, message = "Maximum Discount Amount must be positive")
    private Double maxDiscountAmt;

    @NotNull(message = "Maximum Usable Quantity is required")
    @Min(value = 1, message = "Maximum Usable Quantity must be at least 1")
    private Integer maxUsableQuantity;

    @NotNull(message = "Usage Limit Per User is required")
    @Min(value = 1, message = "Usage Limit Per User must be at least 1")
    private Integer usageLimitPerUser;

    private Integer couponUsedQty;

    private Integer createdBy;

    private Integer updatedBy;

    @NotNull(message = "Start date is required")
    private Date startsAt;

    @NotNull(message = "Expiry date is required")
    private Date expiresAt;

    @NotNull(message = "Active status is required")
    private Boolean isActive;


}
