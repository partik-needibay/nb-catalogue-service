package com.needibay.cart.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nb_product_link")
@Data
public class ProductLink implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Integer product_id;

    @Column(name = "linked_product_id")
    private Integer linked_product_id;

    @Column(name = "link_type")
    private String link_type;
}
