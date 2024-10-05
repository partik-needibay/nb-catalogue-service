package com.needibay.cart.repository.address;

import com.needibay.cart.entity.address.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressTypeRepo extends JpaRepository<AddressType, Long> {
}
