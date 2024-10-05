package com.needibay.cart.controller.order;

import com.needibay.cart.dto.order.OrderByCartIdDTO;
import com.needibay.cart.dto.response.OrderPlacementByCartIdDTO;
import com.needibay.cart.entity.order.SalesOrder;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.order.SalesOrderService;
import com.needibay.cart.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    SalesOrderService salesOrderService;

    @PostMapping("/order")
    public ResponseEntity<Response> orderByCartId(@Valid  @RequestBody OrderByCartIdDTO orderByCartIdDTO, HttpServletRequest request){

        OrderPlacementByCartIdDTO salesOrder = salesOrderService.getFeature().getOrderFeature().placeOrder(orderByCartIdDTO);

        return new ResponseEntity<Response>(new Response.Build()
                .setData(salesOrder).setSuccess(true).setMessage("Order has been placed successfully!")
                .build(), HttpStatus.OK);
        /*return ResponseEntity.ok(new Response.Build()
                .setData(salesOrder).setSuccess(true).setMessage("Order has been placed successfully!")
                .build());*/

    }
    //tested
    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<Response> findOrdersByCustomerId(@PathVariable Integer customerId,
                                                           @RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "sort", required = false) String sortBy,
                                                           @RequestParam(value = "order", required = false) String sortOrder){

        Pageable pageable = new PaginationBuilder.Build().setPageSequence(page == null ? 0 : page).setPageSize(10).build();
        Page<SalesOrder> salesOrder = salesOrderService.getFeature()
                .getOrderFeature()
                .findOrdersByCustomerId(pageable, customerId);

        if (salesOrder == null || salesOrder.isEmpty()) {
            throw new PartialContentException("Partial content error: Orders for customer ID '" + customerId + "' could not be fetched.");
        }

        Response response = new Response.Build()
                .setData(salesOrder)
                .setSuccess(true)
                .setMessage("Orders fetched successfully!")
                .build();

        return ResponseEntity.ok(response);

    }
    //tested
    @GetMapping("/customer/{customerId}/orders/{orderId}")
    public ResponseEntity<Response> findOrdersByCustomerIdAndOrderId(@PathVariable Integer customerId, @PathVariable Integer orderId){

        SalesOrder salesOrder = salesOrderService.getFeature()
                .getOrderFeature()
                .findOrdersByCustomerIdAndOrderId(customerId, orderId);

        if (salesOrder == null) {
            throw new PartialContentException("Partial content error: Order with ID '" + orderId + "' for customer ID '" + customerId + "' could not be fetched.");
        }

        Response response = new Response.Build()
                .setData(salesOrder)
                .setSuccess(true)
                .setMessage("Order fetched successfully!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
