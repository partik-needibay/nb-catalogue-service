package com.needibay.cart.entity.cart;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "eav_entity_store")
@Entity
public class EavEntityStore {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "increment_prefix")
    private String incrementPrefix;

    @Column(name = "increment_last_id")
    private Long incrementLastId;

}
