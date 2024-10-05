package com.needibay.cart.search.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String filterKey;
    private String attributeValue;
    private Object value;
    private String operation;
    private String dataOption;

    public SearchCriteria(String filterKey, String operation, Object value, String attributeValue){
        super();
        this.filterKey = filterKey;
        this.operation = operation;
        this.attributeValue = attributeValue;
        this.value = value;
    }
}
