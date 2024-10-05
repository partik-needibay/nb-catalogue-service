package com.needibay.cart.entity.shipment;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nb_delivery_type")
@Data
public class DeliveryType implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="delivery_type")
    private String deliveryType;
}
