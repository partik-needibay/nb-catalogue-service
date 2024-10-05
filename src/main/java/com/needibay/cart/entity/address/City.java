package com.needibay.cart.entity.address;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="nb_dir_city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="city_name")
    private String cityName;

    @Column(name="city_code")
    private String cityCode;

    @Column(name="country_id")
    private int countryId;

    @Column(name="state_id")
    private int stateId;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    public City(){}


}
