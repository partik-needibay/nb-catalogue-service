package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.needibay.cart.dto.ProductAttributeDTO;
import com.needibay.cart.dto.ProductDTO;
import com.needibay.cart.entity.shipment.DeliveryType;
import lombok.Data;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "nb_product")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product implements Serializable, Cloneable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "parent_config_product_id")
    private Integer parentConfigProductId;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_name")
    private String productName;

/*    @Column(name = "store_visibility")
    private Boolean isStoreVisible;*/

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "base_price", length = 10, precision = 2)
    private Double basePrice;

    @Transient
    private Double basePriceWithCommission;

    @Column(name = "base_commission", length = 12, precision = 2)
    private Double baseCommission;

    @Column(name = "offer_code")
    private String offerCode;

    @Column(name = "base_commission_type")
    private String baseCommissionType;

    @Column(name = "hsn_code")
    private Integer hsnCode;

    @Column(name = "tax_id")
    private Integer taxId;

    @Column(name = "tax_preference")
    private Integer taxPreference;

    @Column(name = "is_discounted")
    private Boolean isDiscounted;

    @Column(name = "store_visibility")
    private Boolean isStoreVisible;

    @Column(name = "is_sample_enable")
    private Boolean isSampleEnable;

    @Column(name = "delivery_charge")
    private Double deliveryCharge;

    @Column(name = "price_with_commission")
    private Double priceWithCommission;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    

    @Column(name = "search_keywords")
    private String searchKeywords;

    @Column(name = "url_key")
    private String urlKey;

    @Column(name = "tax_amount", length = 12, precision = 2)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double taxAmount;

    @Column(name = "tax_percent", length = 12, precision = 2)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double taxPercent;

    @Column(name = "is_customizable")
    private Boolean isCustomizable;

    @Column(name = "is_branded")
    private Boolean isBranded;

    @Column(name = "has_option")
    private Boolean hasOption;

    @Column(name = "is_variant")
    private Boolean isVariant;

    @Column(name = "gen_one")
    private String genOne;

    @Column(name = "gen_two")
    private String genTwo;

    @Column(name = "gen_three")
    private String genThree;

    @Column(name = "gen_four")
    private String genFour;

    @Column(name = "gen_five")
    private String genFive;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProductMediaGallery> media;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProductEavAttributeValue> extendedAttributes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "link_type = 'RELATED_PRODUCT'")
    private List<ProductLink> relatedProducts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<ProductDynamicPricing> productDynamicPricing;

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "nb_product_relation",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    @Fetch(FetchMode.SELECT)
    private List<Product> productVariation = new ArrayList<>();

    @Transient
    private List<ProductEavAttributeOption> productEavAttributeOptions;

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "nb_product_eav_attribute_bridge",
            joinColumns = @JoinColumn(name = "configurable_product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    @Fetch(FetchMode.SELECT)
    private List<ProductEavAttribute> productAttributeOption = new ArrayList<>();

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "nb_product_delivery_bridge",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_type_id")
    )
    @Fetch(FetchMode.SELECT)
    private List<DeliveryType> productDeliveryType = new ArrayList<>();

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentProduct", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProductEavAttributeValue> productAttributes;*/

    /*@Transient
    private ProductAttributeDTO productAttributeOption;*/

    @Transient
    private String productDeliveryTypeString;

    @Transient
    private String minOrderQty;

    @Transient
    @Where(clause = "productType != configurable")
    private Boolean isVariationDefault;


    @Transient
    private Integer variationCount;

    public Product() {}

    public String getProductDeliveryTypeString(){
        String concatProductDeliveryType = "";
        for (DeliveryType obj: productDeliveryType){
            concatProductDeliveryType += obj.getId() + ", ";
        }

        return concatProductDeliveryType;
    }

    public Double getBasePriceWithCommission(){

        Double basePriceWithCommission = this.getBasePrice();

        String commissionType = this.getBaseCommissionType();

        if(commissionType.equals("percent")){

            Double commission = this.getBaseCommission();
            basePriceWithCommission += (basePriceWithCommission * commission) / 100;
        }

        if(commissionType.equals("flat")){

            Double commission = this.getBaseCommission();

            basePriceWithCommission += commission;
        }

        return basePriceWithCommission;
    }

    public Integer getVariationCount() {
        return productVariation.size();
    }


    public List<ProductEavAttribute> getProductAttributeOption(){

        Collections.sort(this.productAttributeOption, Comparator.comparingInt(ProductEavAttribute::getSortOrder));

        return this.productAttributeOption;

    }

    public String getMinOrderQty(){
        String moqValue = "";
        Optional<ProductEavAttributeValue> moq = this.extendedAttributes.stream().filter(option -> option.getFieldType().equals("static") && option.getAttributeCode().equals("moq")).findFirst();
        if(moq.isPresent()){
            moqValue = moq.get().getValue();
        }
        return moqValue;
    }

    public Boolean getIsVariationDefault() {
        Boolean isVariationDefault = false;
        Optional<ProductEavAttributeValue> isDefault = this.extendedAttributes.stream().filter(option -> option.getFieldType().equals("static") && option.getAttributeCode().equals("isVariationDefault")).findFirst();
        if(isDefault.isPresent()){
            if(isDefault.get().getValue().equals("true")){
                isVariationDefault = true;
            }
        }
        return isVariationDefault;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
