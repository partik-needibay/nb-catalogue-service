package com.needibay.cart.entity.status;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nb_status")
@Data
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "status")
    private String status;

    @Column(name = "label")
    private String label;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name="status_type")
    private StatusType statusTypeTable;

    @Column(name = "text_color_code")
    private String textColorCode;

    @Column(name = "background_color_code")
    private String backgroundColorCode;
}
