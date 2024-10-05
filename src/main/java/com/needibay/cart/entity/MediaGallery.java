package com.needibay.cart.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "nb_media_gallery")
@Entity
public class MediaGallery implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "model_entity_id")
    private Integer modelEntityId;

    @Column(name ="value")
    private String value;

    @Column(name ="media_type")
    private String mediaType;

    @Column(name ="media_path")
    private String mediaPath;

    @Column(name ="page_block_code")
    private String pageBlockCode;

    @Column(name ="sort")
    private String sort;

    @Column(name ="display_size")
    private String displaySize;

    @Column(name ="disabled")
    private String disabled;


}
