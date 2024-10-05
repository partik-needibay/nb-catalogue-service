package com.needibay.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeDTO {

    private String fieldType;

    private String fieldLabel;

    private String attributeCode;

    private List<Option> options;

    @Data
    public static class Option {
        private String value;
        private String label;
    }
}
