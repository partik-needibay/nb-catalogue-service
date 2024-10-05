package com.needibay.cart.repository.invoice;

import com.needibay.cart.entity.invoice.Invoice;
import com.needibay.cart.entity.invoice.InvoiceV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepo extends JpaRepository<InvoiceV2, Long> {
    @Query(value = "select * from `nb_sales_invoice_v2` where customer_id = :customerId ORDER BY `nb_sales_invoice_v2`.`id` DESC", nativeQuery = true)
    public List<InvoiceV2> findInvoiceByCustomerId(Long customerId);

    @Query(value = "select * from `nb_sales_invoice_v2` where reserved_order_id = :orderId ORDER BY `nb_sales_invoice_v2`.`id` DESC", nativeQuery = true)
    public InvoiceV2 findInvoiceByOrderId(Long orderId);

}
