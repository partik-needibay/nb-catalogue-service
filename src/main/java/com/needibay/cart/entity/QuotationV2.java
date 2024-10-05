package com.needibay.cart.entity;

import com.needibay.cart.entity.QuotationItemV2;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nb_sales_quotation_v2")
@Data
public class QuotationV2 implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quotation_id")
    private String quotationId;

    @Column(name = "is_active")
    private  Boolean isActive;

    /*Order entity transfer*/
    @Column(name = "is_multi_shipping")
    private Boolean isMultiShipping;

    @Column(name = "items_count")
    private Integer itemsCount;

    @Column(name = "items_qty")
    private Double itemsQty;

    @Column(name = "original_order_id")
    private Integer originalOrderId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "checkout_method")
    private String checkoutMethod;


    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "is_coupon_applied")
    private Boolean isCouponApplied;

    @Column(name = "coupon_discount_amount")
    private Double couponDiscountAmount;

    @Column(name = "discounted_grand_total")
    private Double discountedGrandTotal;

    @Column(name = "discounted_tax_amount")
    private Double discountedTaxAmount;

    @Column(name = "total_discount_amount")
    private Double totalDiscountAmount;

    @Column(name = "offer_discount_amount")
    private Double offerDiscountAmount;

    @Column(name = "offer_code")
    private String offerCode;

    @Column(name = "subtotal_with_discount")
    private Double subtotalWithDiscount;

    /*Order entity transfer*/

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_group_id")
    private Integer customerGroupId;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_fullname")
    private String customeFullname;

    @Column(name = "reserved_order_id")
    private String reserveOrderId;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", orphanRemoval = true)
    @Where(clause = "has_sub_items = true")
    private List<QuotationItemV2> quotationItems = new ArrayList<>();

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", orphanRemoval = true)
    private List<QuotationV2> allQuotationV2s  = new ArrayList<>() ;*/

    public void setQuotationV2(List<QuotationItemV2> j){
        for (QuotationItemV2 obj:j){
            quotationItems.add(obj);
            obj.setQuotation(this);
        }
    }

    @Transient
    public QuotationItemV2 getQuotationV2ById(Long id){
        QuotationItemV2 newObj = null;
        for (QuotationItemV2 obj: this.quotationItems){
            if(obj.getId().equals(id)){
                newObj = obj;

            }
        }
        return newObj;
    }

}