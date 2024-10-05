package com.needibay.cart.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nb_product_category")
@Data
public class Category {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "parent_category_id")
    private Integer parentCategoryId;

    @Column(name="category_name")
    private String categoryName;

    @Column(name = "category_slug")
    private String categorySlug;

    @Column(name = "category_description")
    private String CategoryDescription;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "media_path")
    private String mediaPath;

    @Column(name = "store_visibility")
    private Boolean isStoreVisible;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "model = 'PRODUCT_CATEGORY'")
    private List<MediaGallery> categoryImages;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentCategoryId", fetch = FetchType.LAZY)
    private List<Category> subCategories;

    public Category(){}
}
