package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductOfferRepo extends JpaRepository<ProductOffer, Long> {

    @Query(value = "SELECT * FROM nb_product_offer WHERE offer_code = :offerCode",
            nativeQuery = true)
    ProductOffer findProductOfferByOfferCode(@Param("offerCode") String offerCode);


}
