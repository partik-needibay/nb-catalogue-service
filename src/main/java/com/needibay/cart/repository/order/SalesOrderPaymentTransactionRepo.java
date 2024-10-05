package com.needibay.cart.repository.order;

import com.needibay.cart.entity.order.SalesOrderPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderPaymentTransactionRepo extends JpaRepository<SalesOrderPaymentTransaction, Integer> {
}
