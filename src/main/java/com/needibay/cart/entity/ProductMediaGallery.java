package com.needibay.cart.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "nb_product_entity_media_gallery")
@Entity
public class ProductMediaGallery {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer attributeId;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private Product product;

    private String value;

    private String mediaPath;

    private String mediaType;

    private Integer sort;

    private String displaySize;

    public Integer getProduct() {
        return product.getId();
    }
}
