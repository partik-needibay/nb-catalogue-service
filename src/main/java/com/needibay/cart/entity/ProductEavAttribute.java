package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "nb_product_eav_attribute")
@Entity
public class ProductEavAttribute implements Serializable{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productEavAttribute", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProductEavAttributeOption> attributeOption;

    @Column(name="entity_type_id")
    private Integer entityTypeId;

    @Column(name="attribute_code")
    private String attributeCode;

    @Column(name="frontend_input")
    private String frontendInput;

    @Column(name="frontend_label")
    private String frontendLabel;

    @Column(name="backend_type")
    private String backendType;

    @Column(name="sort_order")
    private Integer sortOrder;


}
