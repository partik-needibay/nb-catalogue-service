package com.needibay.cart.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class VendorAccountDTO {
    @NotBlank(message = "First Name is required")
    @Size(min = 3, message = "First Name must be at least 3 characters long")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 3, message = "Last Name must be at least 3 characters long")
    private String lastName;

    @NotNull(message = "GST Treatment is required")
    private Integer gstTreatment;

    private String gstDetails;

    @NotBlank(message = "Payment Terms are required")
    private String paymentTerms;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    private String gstReg;

    private String pan;

    @Pattern(
            regexp = "((https?):\\/\\/)?(www\\.)?[a-z0-9]+(\\.[a-z]{2,}){1,3}(#?\\/?[a-zA-Z0-9#]+)*\\/?(\\?[a-zA-Z0-9-_]+=[a-zA-Z0-9-%]+&?)?$",
            message = "Enter correct URL"
    )
    private String website;
}
