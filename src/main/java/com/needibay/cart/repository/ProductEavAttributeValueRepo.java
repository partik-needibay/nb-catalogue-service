package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductEavAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEavAttributeValueRepo extends JpaRepository<ProductEavAttributeValue, Integer> {

    @Query(value = "select * from `nb_product_eav_attribute_value` where entity_id = :productId and attribute_code = :attributeCode",
            nativeQuery = true)
    ProductEavAttributeValue findEavAttributeValueByEntityIdAndAttributeCode(String attributeCode, Integer productId);
}
