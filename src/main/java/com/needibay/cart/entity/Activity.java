package com.needibay.cart.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "nb_activities")
@Data
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "model_entity_type")
    private String modelEntityType;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "activity_log")
    private String activityLog;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
