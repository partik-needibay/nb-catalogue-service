package com.needibay.cart.repository;

import com.needibay.cart.entity.PurchaseOrderV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepo extends JpaRepository<PurchaseOrderV2, Long> {
}
