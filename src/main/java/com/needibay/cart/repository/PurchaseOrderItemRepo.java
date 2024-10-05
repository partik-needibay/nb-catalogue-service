package com.needibay.cart.repository;

import com.needibay.cart.entity.PurchaseOrderItemV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderItemRepo extends JpaRepository<PurchaseOrderItemV2, Long> {
}
