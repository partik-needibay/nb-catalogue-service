package com.needibay.cart.repository.address;

import com.needibay.cart.entity.address.Zipcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZipcodeRepo extends JpaRepository<Zipcode, Long> {
}
