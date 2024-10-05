package com.needibay.cart.repository.shipment;

import com.needibay.cart.entity.shipment.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTypeRepo extends JpaRepository<DeliveryType, Long> {
}
