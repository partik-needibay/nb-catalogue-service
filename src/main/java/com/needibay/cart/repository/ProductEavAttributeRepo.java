package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductEavAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductEavAttributeRepo extends JpaRepository<ProductEavAttribute, Integer> {

    @Query(
            value = "SELECT * FROM nb_product_eav_attribute WHERE backend_input IN ('TEXTFIELD', 'TOGGLESWITCH')",
            nativeQuery = true)
    List<ProductEavAttribute> findAttrByBackendInput();

    @Query(
            value = "SELECT * FROM nb_product_eav_attribute WHERE frontend_input IN ('select', 'selectDropdown')",
            nativeQuery = true)
    List<ProductEavAttribute> findProductConfAttr();
}
