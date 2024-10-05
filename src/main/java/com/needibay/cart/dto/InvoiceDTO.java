package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

@Data
@JsonPOJOBuilder
public class InvoiceDTO {
    private String invoiceId;
    private Integer customerId;
    private Integer salesPersonId;
    private String customerNotes;
    private String terms;
    private Integer paymentTerms;
    private String items;
    private String dateIssued;
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
