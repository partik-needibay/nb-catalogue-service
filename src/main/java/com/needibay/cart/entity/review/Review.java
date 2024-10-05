package com.needibay.cart.entity.review;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "nb_reviews")
public class Review implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="model")
    private String model;

    @Column(name ="model_entity_id")
    private Integer modelEntityId;

    @Column(name ="review_type")
    private String reviewType;

    @Column(name ="customer_id")
    private Integer customerId;

    @Column(name ="rating")
    private Float rating;

    @Column(name ="review")
    private String review;

    @Column(name ="is_approved")
    private Boolean isApproved;

}
