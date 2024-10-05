package com.needibay.cart.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "nb_bank_details")
@Entity
public class Bank implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="company_id")
    private Integer companyId;

    @Column(name="bank_name")
    private String bankName;

    @Column(name="bank_account_number")
    private String bankAccountNumber;

    @Column(name="bank_account_name")
    private String bankAccountName;

    @Column(name="ifsc")
    private String ifsc;

    @Column(name="bank_branch")
    private String bankBranch;

//    @Column(name="bank_account_type")
//    private String bankAccountType;

    public Bank(){}
}
