package com.needibay.cart.repository.cart;

import com.needibay.cart.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query(value = "delete from nb_cart_item where id = :id", nativeQuery = true)
    public void deleteById(Integer id);

    @Modifying
    @Query(value = "delete from nb_cart_item where cart_id = :cartId", nativeQuery = true)
    public void deleteCartItemsByCartId(Long cartId);

    @Query(value = "select * from nb_cart_item where cart_id = :cartId", nativeQuery = true)
    public List<CartItem> findByCartId(Integer cartId);
}
