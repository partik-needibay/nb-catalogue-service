package com.needibay.cart.entity.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="nb_dir_zipcode")
public class Zipcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @JsonIgnore
    private Long id;

    @Column(name="zipcode")
    @JsonIgnore
    private String zipcode;

    @Column(name="postal_region_name")
    @JsonIgnore
    private String postalRegionName;

    @Column(name="country_id")
    @JsonIgnore
    private int country;

    @Column(name="state_id")
    @JsonIgnore
    private int state;

    @Column(name="city_id")
    @JsonIgnore
    private int city;

    @Column(name="created_at")
    @CreationTimestamp
    @JsonIgnore
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    @JsonIgnore
    private Date updatedAt;

    @Transient
    private String label;

    @Transient
    private String value;

    public String getLabel() {
        return this.zipcode;
    }

    public Long getValue() {
        return this.id;
    }

}
