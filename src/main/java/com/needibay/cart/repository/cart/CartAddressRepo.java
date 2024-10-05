package com.needibay.cart.repository.cart;

import com.needibay.cart.entity.cart.CartAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartAddressRepo extends JpaRepository<CartAddress, Long> {
}
