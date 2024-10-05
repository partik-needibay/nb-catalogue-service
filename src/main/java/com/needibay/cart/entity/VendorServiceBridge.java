package com.needibay.cart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Table(name = "nb_vendor_service_bridge")
@Entity
public class VendorServiceBridge implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade= CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "vendor_id", insertable = false, updatable = false)
    private Account account;

    @Column(name = "vendor_id")
    private int vendorId;

    @Column(name = "service_id")
    private Integer serviceId;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "service_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EventService service;

    @Column(name = "per_hr_cost")
    private Double perHrCost;

    @Column(name = "hrs")
    private int hrs;

    @Column(name = "rental_cost")
    private Double rentalCost;

    @Column(name = "is_active")
    private Boolean isActive;

    public VendorServiceBridge(){}

    public int getAccount() {
        return account.getId();
    }

    public String getService(){
        return service.getServiceName();
    }
}
