package com.needibay.cart.repository.cart;

import com.needibay.cart.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    @Query(value = "select * from nb_cart where customer_id = :customerId and is_active = true ORDER BY id desc limit 1", nativeQuery = true)
    public Cart findByCustomerId(Integer customerId);

}
