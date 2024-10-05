package com.needibay.cart.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.ProductMediaGallery;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "nb_cart_item")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(name = "cart_id")
    private Integer cartId;*/

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "product_id")
    private Integer productId;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @Transient
    private String productName;

    @Transient
    private String productSlug;

    @Transient
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductMediaGallery productImage;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productImageDefault;

    @Column(name = "parent_item_id")
    private Integer parentItemId;

    @Column(name = "is_sample_qty")
    private Boolean isSampleQty;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "additional_data")
    private String additionalData;

    @Column(name = "is_qty_decimal")
    private Boolean isQtyDecimal;

    @Column(name = "no_discount")
    private Integer noDiscount;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "min_order_qty")
    private Integer minOrderQty;

    @Column(name = "row_total_qty")
    private Integer rowTotalQty;

    @Column(name = "price")
    private Double price;

    @Column(name = "is_coupon_applied")
    private Boolean isCouponApplied;

    @Column(name = "discount_percent")
    private Double discountPercent;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "tax_percent")
    private Double taxPercent;

    @Column(name = "row_total_tax_amount")
    private Double rowTotalTaxAmount;

    @Column(name = "row_total_discounted_tax_amount")
    private Double rowTotalDiscountedTaxAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "row_total")
    private Double rowTotal;

    @Column(name = "row_total_with_discount")
    private Double rowTotalWithDiscount;

    @Column(name = "row_weight")
    private Double rowWeight;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "tax_before_discount")
    private Double taxBeforeDiscount;

    @Column(name = "original_custom_price")
    private Double originalCustomPrice;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "price_incl_tax")
    private Double priceInclTax;

    @Column(name = "row_total_incl_tax")
    private Double rowTotalInclTax;

    @Column(name = "free_shipping")
    private Integer freeShipping;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    public Long getCart(){
        return cart.getId();
    }

    public String getProductName(){
        return product.getProductName();
    }

    public String getProductSlug(){
        return product.getProductSlug();
    }

    public ProductMediaGallery getProductImage(){

        return product.getMedia().get(0);
    }

    public String getProductImageDefault(){
        return getProductImage().getMediaPath();
    }

}
