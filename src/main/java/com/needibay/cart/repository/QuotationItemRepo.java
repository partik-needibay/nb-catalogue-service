package com.needibay.cart.repository;

import com.needibay.cart.entity.QuotationItemV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationItemRepo extends JpaRepository<QuotationItemV2, Long> {
}
