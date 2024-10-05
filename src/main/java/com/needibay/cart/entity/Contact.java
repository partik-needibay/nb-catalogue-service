package com.needibay.cart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name = "nb_contact")
public class Contact implements Serializable {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade= CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Account account;

    @Column(name="company_id")
    private Integer companyId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String LastName;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="is_owner")
    private Boolean isOwner;

    @Column(name="created_by")
    private  int createdBy;

    @Column(name="updated_by")
    private String updatedBy;

    public int getAccount() {
        return account.getId();
    }

    public Contact(){}
}
