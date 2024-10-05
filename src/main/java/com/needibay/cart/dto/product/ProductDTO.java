package com.needibay.cart.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@JsonPOJOBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    private Integer categoryId;

    private String productType;

    private String sku;

    private String productName;

    private String productSlug;

    @Min(value = 0, message = "Base Price must be positive")
    private Double basePrice;

    @Min(value = 0, message = "Base Commission must be positive")
    private Double baseCommission;

    private String baseCommissionType;

    private Integer hsnCode;

    @Min(value = 0, message = "Tax Percent must be positive")
    @Max(value = 100, message = "Tax Percent cannot be more than 100")
    private Double taxPercent;

    private Integer taxPreference;

    private Boolean isVariant;

    private Integer parentConfigProductId = null;

    private Boolean isDiscounted;

    private Boolean isCustomizable;

    private Boolean hasOption;

    private Boolean isActive;

    private Boolean isStoreVisible = false;

    private Boolean isSampleEnable = false;

    private Double deliveryCharge = 0.00;

    private String metaTitle;

    private String metaDescription;

    private String searchKeywords;

    private String urlKey;

    private List<MultipartFile> image;

    private String relatedProducts = null;
    private String productDynamicPricing = null;
    private String extendedAttributes = null;
    private String configuration = null;
    private String confAttributes = null;
    private String shortDescription = null;
    private String longDescription = null;

}
