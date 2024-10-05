package com.needibay.cart.entity.cart;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "nb_cart_address")
public class CartAddress {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "save_in_address_book")
    private Integer saveInAddressBook;

    @Column(name = "customer_address_id")
    private Integer customerAddressId;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "company")
    private String company;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "same_as_billing")
    private Boolean sameAsBilling;

    @Column(name = "collect_shipping_rates")
    private Boolean collectShippingRates;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_description")
    private String shippingDescription;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "subtotal_with_discount")
    private Double subtotalWithDiscount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "shipping_amount")
    private Double shippingAmount;

    @Column(name = "shipping_tax_amount")
    private Double shippingTaxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "customer_notes")
    private String customerNotes;

    @Column(name = "applied_taxes")
    private String appliedTaxes;

    @Column(name = "discount_description")
    private String discountDescription;

    @Column(name = "shipping_discount_amount")
    private Double shippingDiscountAmount;

    @Column(name = "subtotal_incl_tax")
    private Double subtotalInclTax;

    @Column(name = "discount_tax_compensation_amount")
    private Double discountTaxCompensationAmount;

    @Column(name = "shipping_discount_tax_compensation_amount")
    private Double shippingDiscountTaxCompensationAmount;

    @Column(name = "shipping_incl_tax")
    private Double shippingInclTax;

    @Column(name = "free_shipping")
    private Double freeShipping;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

}
