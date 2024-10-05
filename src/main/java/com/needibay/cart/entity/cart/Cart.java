package com.needibay.cart.entity.cart;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "nb_cart")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Cart {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active")
    private  Boolean isActive;

    @Column(name = "is_multi_shipping")
    private Boolean isMultiShipping;

    @Column(name = "items_count")
    private Integer itemsCount;

    @Column(name = "items_qty")
    private Double itemsQty;

    @Column(name = "original_order_id")
    private Integer originalOrderId;

    @Column(name = "currency_code")
    private Integer currencyCode;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "checkout_method")
    private Integer checkoutMethod;

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

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public void setCartItems(List<CartItem> j){
        for (CartItem obj:j){
            cartItems.add(obj);
            obj.setCart(this);
        }
    }

    @Transient
        public CartItem getCartItemById(Long id){
        CartItem newObj = null;
        for (CartItem obj: this.cartItems){
            if(obj.getId().equals(id)){
                newObj = obj;

            }
        }
        return newObj;
    }

}

