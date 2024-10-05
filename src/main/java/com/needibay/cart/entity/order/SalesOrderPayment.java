package com.needibay.cart.entity.order;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="nb_sales_order_payment")
@Data
public class SalesOrderPayment implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private SalesOrder salesOrder;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_status")
    private Integer paymentStatus;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "due_amount")
    private Double dueAmount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "is_refund_initiated")
    private Boolean isRefundInitiated;

    @Column(name = "is_order_active")
    private Boolean isOrderActive;

    public SalesOrderPayment(){}

    public Long getSalesOrder(){
        return salesOrder.getId();
    }
}
