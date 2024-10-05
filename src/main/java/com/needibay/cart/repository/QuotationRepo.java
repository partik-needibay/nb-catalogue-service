package com.needibay.cart.repository;

import com.needibay.cart.entity.QuotationV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepo extends JpaRepository<QuotationV2, Long> {
}
