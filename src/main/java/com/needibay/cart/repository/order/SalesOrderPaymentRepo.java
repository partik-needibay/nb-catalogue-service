package com.needibay.cart.repository.order;

import com.needibay.cart.entity.order.SalesOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderPaymentRepo extends JpaRepository<SalesOrderPayment, Integer> {

    @Query(value = "select * from `nb_sales_order_payment` where customer_id = :customerId ORDER BY `nb_sales_order_payment`.`id` DESC", nativeQuery = true)
    public List<SalesOrderPayment> findPaymentByCustomerId(Long customerId);

    @Query(value = "select * from `nb_sales_order_payment` where order_id = :orderId ORDER BY `nb_sales_order_payment`.`id` DESC", nativeQuery = true)
    public List<SalesOrderPayment> findPaymentByOrderId(Long orderId);
}
