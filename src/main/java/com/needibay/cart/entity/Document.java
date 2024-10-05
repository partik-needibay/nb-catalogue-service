package com.needibay.cart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "nb_documents")
@Entity
public class Document implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="vendor_id")
    private int vendorId;

    @Column(name="type")
    private int type;

    @Column(name="name")
    private String name;

    @Column(name="extension")
    private String extension;

    @Column(name="path")
    private String path;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="created_by")
    private int createdBy;

    @Column(name="updated_by")
    private int updatedBy;
}
