package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductDynamicPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDynamicPricingRepo extends JpaRepository<ProductDynamicPricing, Long> {
    @Query(value = "select * from `nb_product_dynamic_pricing` where product_id = :productId",
            nativeQuery = true)
    List<ProductDynamicPricing> getDynamicPricingByProductId(Integer productId);

    @Query(value = "select * from `nb_product_dynamic_pricing` where product_id = :productId and (min_qty = :minQty or max_qty = :maxQty)", nativeQuery = true)
    List<ProductDynamicPricing> getDynamicPricingByProductIdMinMaxQty(Integer productId, Integer minQty, Integer maxQty);
}
