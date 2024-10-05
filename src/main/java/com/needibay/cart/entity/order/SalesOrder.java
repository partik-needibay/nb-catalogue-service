package com.needibay.cart.entity.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.needibay.cart.entity.address.Address;
import com.needibay.cart.entity.status.Status;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="nb_sales_order_v2")
@Data
public class SalesOrder implements Serializable {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @Column(name="id")
        private Long id;

        @Column(name = "increment_id")
        @OrderBy("increment_id DESC")
        private Integer incrementId;

        /*Copied From cart*/

        @Column(name = "is_multi_shipping")
        private Boolean isMultiShipping;

        @Column(name = "items_count")
        private Integer itemsCount;

        @Column(name = "items_qty")
        private Double itemsQty;


        @Column(name = "currency_code")
        private String currencyCode;

        @Column(name = "grand_total")
        private Double grandTotal;

        @Column(name = "checkout_method")
        private String checkoutMethod;

        @Column(name = "tax_amount")
        private Double taxAmount;

        @Column(name = "created_at")
        @CreationTimestamp
        private Date createdAt;

        @Column(name = "updated_at")
        @CreationTimestamp
        private Date updatedAt;

        @Column(name = "coupon_code")
        private String couponCode;

        @Column(name = "subtotal")
        private Double subtotal;

        @Column(name = "subtotal_with_discount")
        private Double subtotalWithDiscount;

        /*Copied From cart*/

        @Column(name = "customer_id")
        private Integer customerId;

        @Column(name = "customer_group_id")
        private Integer customerGroupId;

        @Column(name = "customer_email")
        private String customerEmail;

        @Column(name = "customer_phone")
        private String customerPhone;

        @Column(name = "customer_fullname")
        private String customerFullName;

        @Column(name = "order_status_id")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Integer orderStatusId;

        @OneToOne
        @JoinColumn(name = "order_status_id", referencedColumnName = "id", insertable = false, updatable = false)
        private Status orderStatus;

/*    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "order_status_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Status orderStatus;*/

        @Column(name = "payment_method_id")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Integer paymentMethodId;

/*        @OneToOne(cascade = CascadeType.DETACH)
        @JoinColumn(name = "payment_method_id", referencedColumnName = "id", insertable = false, updatable = false)
        private PaymentMethod paymentMethod;*/



        @Column(name = "order_amount")
        private Double orderAmount;

        @Column(name = "is_coupon_applied")
        private Boolean isCouponApplied;

        @Column(name = "is_discount_applied")
        private Boolean isDiscountApplied;

        @Column(name = "billing_address")
        private Integer billingAddress;

        @OneToOne
        @JoinColumn(name = "billing_address", referencedColumnName = "id", insertable = false, updatable = false)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Address billingAddressDetails;

        @OneToOne
        @JoinColumn(name = "shipping_address", referencedColumnName = "id", insertable = false, updatable = false)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Address shippingAddressDetails;

        @Column(name = "shipping_address")
        private Integer shippingAddress;

        @Column(name = "is_tax_applied")
        private Boolean isTaxApplied;

        @Column(name = "discount_amount")
        private Boolean discountAmount;

        @Column(name = "is_invoice_generated")
        private Boolean isInvoiceGenerated;

        @Column(name = "is_email_triggered")
        private Boolean isEmailTriggered;

        @Column(name = "is_sms_triggered")
        private Boolean isSmsTriggered;

        @Column(name = "is_active")
        private Boolean isActive;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "salesOrder", fetch = FetchType.LAZY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Fetch(FetchMode.SELECT)
        private List<SalesOrderItem> salesOrderItem;



        @OneToMany(cascade = CascadeType.ALL, mappedBy = "salesOrder", fetch = FetchType.LAZY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Fetch(FetchMode.SELECT)
        private List<SalesOrderPayment> salesOrderPayments;

}

