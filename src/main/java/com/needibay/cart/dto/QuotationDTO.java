package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonPOJOBuilder
public class QuotationDTO {
    private String quotationId;

    @NotNull(message = "Please Select Customer")
    private Integer customerId;

    @NotNull(message = "Invoice Id is required")
    private Integer salesPersonId;

    @NotBlank(message = "Customer Notes are required")
    private String customerNotes;

    @NotBlank(message = "Terms are required")
    private String terms;

    @NotNull(message = "Payment Terms are required")
    private Integer paymentTerms;

    @NotBlank(message = "Items are required")
    private String items;

    @NotBlank(message = "Date Issued is required")
    private String dateIssued;

    @NotBlank(message = "Due Date is required")
    private String dueDate;

    private Double subtotal;
    private Double subtotalWithDiscount;
    private Double discountAmount;
    private Double discount;
    private String discountType;
    private Double adjustment;
    private String adjustmentType;
    private Double grandTotal;
    private Double tax;
    private String customerEmail;
    private String customerPhone;
    private String customerFullName;
}
