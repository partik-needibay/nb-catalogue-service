package com.needibay.cart.entity.invoice;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "nb_sales_invoice")
@Data
public class Invoice implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "subtotal_incl_tax")
    private Double subtotalInclTax;

    @Column(name = "total_qty")
    private Double totalQty;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "billing_address_id")
    private Integer billingAddressId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "email_sent")
    private Boolean emailSent;

    @Column(name = "can_void_flag")
    private Boolean canVoidFlag;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "order_currency_code")
    private String orderCurrencyCode;

    @Column(name = "global_currency_code")
    private String globalCurrencyCode;

    @Column(name = "increment_id")
    private String incrementId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "discount_tax_compensation_amount")
    private Double discountTaxCompensationAmount;

    @Column(name = "shipping_discount_tax_compensation_amount")
    private Double shippingDiscountTaxCompensationAmount;

    @Column(name = "shipping_incl_tax")
    private Double shippingInclTax;

    @Column(name = "discount_description")
    private String discountDescription;

    @Column(name = "customer_note")
    private String customerNote;

    @Column(name = "customer_note_notify")
    private Boolean customerNoteNotify;

}
