package com.needibay.cart.repository.order;

import com.needibay.cart.entity.order.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepo extends JpaRepository<SalesOrder, Long> {

    @Query(value = "select * from `nb_sales_order_v2` where customer_id = :customerId ORDER BY `nb_sales_order_v2`.`increment_id` DESC",
            countQuery = "select count(*) from nb_sales_order_v2 where customer_id = :customerId",
    nativeQuery = true)
    Page<SalesOrder> findOrdersByCustomerId(Pageable pageable, Integer customerId);

    @Query(value = "select * from nb_sales_order_v2 where customer_id = :customerId and id = :orderId ORDER BY 'increment_id' DESC",
            nativeQuery = true)
    SalesOrder findOrdersByCustomerIdAndOrderId(Integer customerId, Integer orderId);
    @Query(value = "select max(increment_id) from nb_sales_order_v2", nativeQuery = true)
    Integer findMaxIncrementId();
}
