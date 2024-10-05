package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

@Data
@JsonPOJOBuilder
public class MediaGalleryPageBlockUpdateDTO {

    public Long id;
    public String pageBlockCode;
}
