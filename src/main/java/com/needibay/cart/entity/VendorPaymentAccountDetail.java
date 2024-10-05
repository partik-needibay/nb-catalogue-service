package com.needibay.cart.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "nb_company")
@Entity
public class VendorPaymentAccountDetail {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    public VendorPaymentAccountDetail(){}
}
