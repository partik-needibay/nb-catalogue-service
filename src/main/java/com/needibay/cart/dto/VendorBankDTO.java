package com.needibay.cart.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VendorBankDTO {
    public Integer accountId;

    @NotBlank(message = "Bank Name is required")
    private String bankName;

    @NotBlank(message = "Bank Account Number is required")
    private String bankAccountNumber;

    @NotBlank(message = "Bank Account Name is required")
    private String bankAccountName;

    @NotBlank(message = "IFSC is required")
    private String ifsc;

    @NotBlank(message = "Bank Branch is required")
    private String bankBranch;
}
