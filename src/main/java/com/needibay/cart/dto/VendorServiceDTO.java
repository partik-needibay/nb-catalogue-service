package com.needibay.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class VendorServiceDTO {

    private Integer vendorId;

    private List<EventServiceDTO> services;

    public VendorServiceDTO(){}

}
