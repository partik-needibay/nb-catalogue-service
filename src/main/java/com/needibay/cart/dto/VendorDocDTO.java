package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@JsonPOJOBuilder
public class VendorDocDTO {

    public Integer accountId;
    private List<MultipartFile> docs;
    private List<String> documentType;

}
