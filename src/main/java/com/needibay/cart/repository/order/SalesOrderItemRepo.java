package com.needibay.cart.repository.order;

import com.needibay.cart.entity.order.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderItemRepo extends JpaRepository<SalesOrderItem, Integer> {
}
