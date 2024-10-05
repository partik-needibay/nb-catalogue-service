package com.needibay.cart.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "nb_product_offer")
public class  ProductOffer {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_code")
    private String offerCode;

    @Column(name = "offer_start_date")
    private String offerStartDate;

    @Column(name = "offer_end_date")
    private String offerEndDate;

    @Column(name = "offer_discount_type")
    private String offerDiscountType;

    @Column(name = "offer_discount")
    private String offerDiscount;

    @Column(name = "is_cart")
    private String isCart;

    @Column(name = "min_cart_amount")
    private String minCartAmount;

    @Column(name = "is_coupon_applicable")
    private String isCouponApplicable;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "is_clock_ticker_enabled")
    private String isClockTickerEnabled;

    @Column(name = "offer_card_title")
    private String offerCardTitle;

    @Column(name = "offer_card_subtitle")
    private String offerCardSubtitle;

    @Column(name = "offer_card_image")
    private String offerCardImage;

    @Column(name = "offer_card_page_redirection_link")
    private String offerCardPageRedirectionLink;

    @Column(name = "offer_start_time")
    private String offerStartTime;

    @Column(name = "offer_end_time")
    private String offerEndTime;

}
