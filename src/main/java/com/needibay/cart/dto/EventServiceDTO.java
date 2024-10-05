package com.needibay.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventServiceDTO {
    private Integer serviceId;

    private Double perHrCost;

    private Integer hrs;

    private Double rentalCost;

    private Integer tableReq;

    private Integer powerReq;

    private Integer speakerMicReq;

    private Integer participantCount;

    private String comments;

    private Boolean isActive;

    public EventServiceDTO(){}
}
