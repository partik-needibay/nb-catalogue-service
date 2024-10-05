package com.needibay.cart.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "nb_service_category")
@Entity
public class EventServiceCategory {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "parent_category_id")
    private Integer parentCategoryId;

    @Column(name = "is_active")
    private Boolean isActive;

    public EventServiceCategory(){}
}
