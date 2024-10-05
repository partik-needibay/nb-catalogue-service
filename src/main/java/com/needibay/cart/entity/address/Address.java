package com.needibay.cart.entity.address;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "nb_addresses")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="model")
    private String modelName;

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @Where(clause = "model = Customer")
    private Customer customer;*/

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name="address_line_one")
    private String AddressLineOne;

    @Column(name="address_line_two")
    private String AddressLineTwo;

    @Column(name="address_line_three")
    private String AddressLineThree;

    @Column(name="street")
    private String Street;

    @Column(name="locality")
    private String locality;

    @Column(name="landmark")
    private String landmark;

    @Column(name="house")
    private String house;

    @Column(name="building")
    private String building;

    @Column(name="flat")
    private String flat;

    @Column(name="floor")
    private String floor;

    @Column(name="city")
    private Integer city;

    @Column(name="state")
    private Integer state;

    @Column(name="country")
    private Integer country;

    @Column(name="zipcode")
    private Integer zipcode;

    @Column(name="phone")
    private String phone;

    @Column(name="contact_person")
    private String contactPerson;

    @Column(name="g_map_formatted_address")
    private String gMapFormattedAddress;

    @Column(name="g_map_lat")
    private String gMapLat;

    @Column(name="g_map_lng")
    private String gMapLng;

    @Column(name="is_default")
    private boolean isDefault;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @Column(name = "address_type_id")
    private Integer addressType;

}
