package com.needibay.cart.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.ProductMediaGallery;
import com.needibay.cart.entity.cart.Cart;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="nb_sales_order_item_v2")
@Data
public class SalesOrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "order_id")
    private Integer orderId;

    /*@ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private SalesOrder salesOrder;*/

    @Column(name = "product_id")
    private Integer productId;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "increment_id", insertable = false, updatable = false)
    private SalesOrder salesOrder;

    @Transient
    private String productName;

    @Transient
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductMediaGallery productImage;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productImageDefault;

    @Column(name = "parent_item_id")
    private Integer parentItemId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

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

    @Column(name = "discount_percent")
    private Double discountPercent;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "tax_percent")
    private Double taxPercent;

    @Column(name = "row_total_tax_amount")
    private Double rowTotalTaxAmount;

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

    public String getProductName(){
        return product.getProductName();
    }

    public ProductMediaGallery getProductImage(){
        if(product.getMedia().size() > 0 && product.getMedia().get(0) != null){
            return product.getMedia().get(0);
        }
        else{
            return null;
        }
    }

    public String getProductImageDefault(){
        if(getProductImage() != null){
            return getProductImage().getMediaPath();
        }
        else{
            return null;
        }

    }

    public Integer getSalesOrder(){
        return salesOrder.getIncrementId();
    }

}
