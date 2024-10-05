package com.needibay.cart.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ReviewDTO {

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Rating is required")
    private Float rating;

    @NotBlank(message = "Review is required")
    private String review;

}
