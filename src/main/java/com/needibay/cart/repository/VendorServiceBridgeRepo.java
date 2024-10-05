package com.needibay.cart.repository;

import com.needibay.cart.entity.VendorServiceBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorServiceBridgeRepo extends JpaRepository<VendorServiceBridge, Integer> {
}
