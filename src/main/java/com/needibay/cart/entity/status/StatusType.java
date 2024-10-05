package com.needibay.cart.entity.status;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "nb_status_types")
@Data
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "status_type")
    private String statusType;

    @Column(name = "status_type_label")
    private String statusTypeLabel;

}
