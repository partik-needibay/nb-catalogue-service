package com.needibay.cart.entity.coupon;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "nb_coupons")
public class Coupon implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_type")
    private Integer serviceType;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "percent")
    private Double percent;

    @Column(name = "min_order_price")
    private Double minOrderPrice;

    @Column(name = "has_days_availibility")
    private Boolean hasDaysAvailibility;

    @Column(name = "has_dates_availibity")
    private Boolean hasDatesAvailibity;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_category_id")
    private Integer productCategoryId;

    @Column(name = "customer_group")
    private Integer customerGroup;

    @Column(name = "max_discount_amt")
    private Double maxDiscountAmt;

    @Column(name = "max_usable_quantity")
    private Integer maxUsableQuantity;

    @Column(name = "usage_limit_per_user")
    private Integer usageLimitPerUser;

    @Column(name = "coupon_used_qty")
    private Integer couponUsedQty;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "starts_at")
    private Date startsAt;

    @Column(name = "expires_at")
    private Date expiresAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_code", referencedColumnName = "coupon_code")
    private List<CouponUserLog> couponUserLogs;

}
