package com.needibay.cart.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonPOJOBuilder
public class ProductCategoryDTO {
    /*private Integer id;*/

    private String categoryName;


    private List<MultipartFile> image;


    private Integer vendorId;

    private Integer parentId;


    private Boolean isActive;


    private String description;


    private String metaDescription;


    private String metaTitle;


    private String categoryType;


    private String categorySlug;


    private Boolean isStoreVisible;
}
