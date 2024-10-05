package com.needibay.cart.repository;

import com.needibay.cart.entity.PaymentTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTermsRepo extends JpaRepository<PaymentTerms, Long> {
}
