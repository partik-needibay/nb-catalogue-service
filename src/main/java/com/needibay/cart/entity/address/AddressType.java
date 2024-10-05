package com.needibay.cart.entity.address;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "nb_address_type")
@Entity
public class AddressType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name= "address_type_name")
    private String addressTypeName;

    @Column(name = "address_type_label")
    private String addressTypeLabel;

    @Column(name = "has_parent")
    private boolean hasParent;

    @Column(name = "parent_id")
    private int parentId;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

}
