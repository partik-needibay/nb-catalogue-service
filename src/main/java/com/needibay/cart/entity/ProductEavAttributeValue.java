package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "nb_product_eav_attribute_value")
@Entity
public class ProductEavAttributeValue implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "parent_entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Product parentProduct;

    @OneToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductEavAttribute productEavAttribute;

    /*@OneToOne
    @JoinColumn(name = "value", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductEavAttributeOption productEavAttributeOption;*/

    /*
     * Product sku unique with respect to each attribute and it's value
     * */
    @Column(name="entity_id_sku")
    private String entityIdSku;

    @Column(name="attribute_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int attributeId;

    @Column(name = "attribute_code")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String attributeCode;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fieldType;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fieldLabel;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String valueDataType;


    @Column(name="entity_id")
    @JsonIgnore
    private Integer entityId;

    @Column(name="has_mapping")
    @JsonIgnore
    private Boolean hasMapping;

    @Column(name="value")
    private String value;

    @Column(name="value_id")
    private String valueId;

    @Column(name="sort_order")
    private String sortOrder;

    public Integer getProduct() {
        return product.getId();
    }

    public Integer getParentProduct() {
        return parentProduct.getId();
    }

/*    public String getAttributeCode() {
        return productEavAttribute.getAttributeCode();
    }*/

    public String getFieldLabel() {
        return productEavAttribute.getFrontendLabel();
    }

    public String getFieldType() {
        return productEavAttribute.getFrontendInput();
    }

    public String getValueDataType() {
        return productEavAttribute.getBackendType();
    }

    /*public String getFrontendProductVariationFieldValue(){
        return productEavAttributeOption.getLabel();
    }*/
}
