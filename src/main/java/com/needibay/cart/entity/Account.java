package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.needibay.cart.entity.address.City;
import com.needibay.cart.entity.address.Country;
import com.needibay.cart.entity.address.State;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Table(name = "nb_company")
@Data
@Entity
public class Account implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="gst_treatment")
    private Integer gstTreatment;

    @Column(name="gst_details")
    private String gstDetails;

    @Column(name="payment_terms")
    private String paymentTerms;

    @Column(name="industry")
    private int industry;

    @Column(name="email")
    private String email;

    @Column(name="gst_reg")
    private String gstReg;

    @Column(name="pan")
    private String pan;

    @Column(name="website")
    private String website;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_entity_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "model = 'VENDOR_DOCUMENT'")
    private List<MediaGallery> documents;

/*    @Column(name="reg_address")
    private String regAddress;

    @Column(name="city")
    private Integer city;

    @Column(name="state")
    private Integer state;

    @Column(name="country")
    private Integer country;

    @Column(name="zipcode")
    private String zipcode;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="updated_by")
    private Integer updatedBy;*/

/*    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "city", referencedColumnName = "id", insertable = false, updatable = false)
    private City cityName;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "state", referencedColumnName = "id", insertable = false, updatable = false)
    private State stateName;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "country", referencedColumnName = "id", insertable = false, updatable = false)
    private Country countryName;*/

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy="account", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Fetch(FetchMode.SELECT)
    private List<Contact> contact;

/*    @OneToMany(cascade = CascadeType.PERSIST, mappedBy="account", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Fetch(FetchMode.SELECT)
    private List<VendorServiceBridge> vendorServices;*/

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;


    public Account(){}

/*    public String getCityName(){
        return cityName.getCityName();
    }

    public String getStateName(){
        return stateName.getStateName();
    }

    public String getCountryName(){
        return countryName.getCountryName();
    }*/
}
