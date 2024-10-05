package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "nb_product_eav_attribute_option")
@Entity
public class ProductEavAttributeOption implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductEavAttributeOption productEavAttribute;

    @Column(name="attribute_id")
    @JsonIgnore
    private Integer attributeId;

    @Column(name="label")
    private String label;

    public Integer getProductEavAttribute(){
        return productEavAttribute.getId();
    }

    @Transient
    private Integer value;

    @Transient
    private Boolean isDisabled;

    public Integer  getValue(){
        return this.id;
    }

}
