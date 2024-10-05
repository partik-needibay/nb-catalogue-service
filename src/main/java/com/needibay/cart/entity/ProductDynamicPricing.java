package com.needibay.cart.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "nb_product_dynamic_pricing")
@Entity
public class ProductDynamicPricing implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vendor_id")
    private Integer vendorId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "min_qty")
    private Integer minQty;

    @Column(name = "max_qty")
    private Integer maxQty;

    @Column(name = "price")
    private Double price;

    @Column(name = "nb_commission_type")
    private Double commissionType;

    @Column(name = "nb_commission")
    private Double commission;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

}
