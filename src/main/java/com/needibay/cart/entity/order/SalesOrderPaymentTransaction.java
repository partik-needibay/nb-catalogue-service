package com.needibay.cart.entity.order;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="nb_sales_order_payment_transaction")
@Data
public class SalesOrderPaymentTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="payment_id")
    private String paymentId;

    @Column(name="gateway_order_id")
    private String gatewayOrderId;

    @Column(name="gateway_payment_id")
    private String gatewayPaymentId;

    @Column(name = "gateway_customer_id")
    private String gatewayCustomerId;

    @Column(name="gateway_invoice_id")
    private String gatewayInvoiceId;

    @Column(name="gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name="gateway_payment_method")
    private String gatewayPaymentMethod;

    @Column(name="gateway_error_code")
    private String gatewayErrorCode;

    @Column(name="gateway_error_description")
    private String gatewayErrorDescription;

    @Column(name="gateway_payment_status")
    private String gatewayPaymentStatus;

    @Column(name="gateway_amount")
    private Double gatewayAmount;

    @Column(name="amount")
    private Double amount;

    @Column(name="status")
    private Integer Status;

    @Column(name="order_id")
    private String orderId;

    @Column(name = "payment_method")
    private Integer paymentMethod;

}
