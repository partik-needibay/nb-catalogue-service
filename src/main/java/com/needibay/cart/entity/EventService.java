package com.needibay.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "nb_service")
@Entity
public class EventService {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "per_hr_cost")
    private Double perHrCost;

    @Column(name = "hrs")
    private Integer hrs;

    @Column(name = "rental_cost")
    private Double rentalCost;

    @Column(name = "table_req")
    private Integer tableReq;

    @Column(name = "power_req")
    private Integer powerReq;

    @Column(name = "speaker_mic_req")
    private Integer speakerMicReq;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EventServiceCategory category;

    @Transient
    private String categoryName;

    public String getCategoryName() {
        return category.getCategoryName();
    }

    public EventService(){}
}
