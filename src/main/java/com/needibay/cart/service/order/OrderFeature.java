package com.needibay.cart.service.order;

import com.needibay.cart.component.IServiceCommand;
import com.needibay.cart.component.PostServiceCommand;
import com.needibay.cart.component.ServiceInvoker;
import com.needibay.cart.dto.OrderStatusUpdateDTO;
import com.needibay.cart.dto.SendEmailDTO;
import com.needibay.cart.dto.order.OrderByCartIdDTO;
import com.needibay.cart.dto.order.OrderBySystem;
import com.needibay.cart.dto.coupon.ApplyCouponByAdminDTO;
import com.needibay.cart.dto.response.OrderPlacementBySystemDTO;
import com.needibay.cart.entity.Activity;
import com.needibay.cart.entity.coupon.Coupon;
import com.needibay.cart.entity.coupon.CouponUserLog;
import com.needibay.cart.repository.ActivityRepo;
import com.needibay.cart.repository.cart.EavEntityStoreRepo;
import com.needibay.cart.repository.coupon.CouponRepo;
import com.needibay.cart.repository.coupon.CouponUserLogRepo;
import com.needibay.cart.response.ApplyCouponResponseDTO;
import com.needibay.cart.service.coupon.CouponFeature;
import com.needibay.cart.dto.response.OrderPlacementByCartIdDTO;
import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.entity.cart.CartItem;
import com.needibay.cart.entity.invoice.InvoiceItemV2;
import com.needibay.cart.entity.invoice.InvoiceV2;
import com.needibay.cart.entity.order.SalesOrder;
import com.needibay.cart.entity.order.SalesOrderItem;
import com.needibay.cart.entity.order.SalesOrderPayment;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.libs.PostRequest;
import com.needibay.cart.repository.address.AddressRepo;
import com.needibay.cart.repository.cart.CartRepo;
import com.needibay.cart.repository.invoice.InvoiceRepo;
import com.needibay.cart.repository.order.SalesOrderItemRepo;
import com.needibay.cart.repository.order.SalesOrderPaymentRepo;
import com.needibay.cart.repository.order.SalesOrderRepo;
import com.needibay.cart.service.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Service
public class OrderFeature implements Feature {

    @Autowired
    SalesOrderRepo salesOrderRepo;

    @Autowired
    SalesOrderItemRepo salesOrderItemRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    SalesOrderPaymentRepo salesOrderPaymentRepo;

    @Autowired
    InvoiceRepo invoiceRepo;

    @Autowired
    private EavEntityStoreRepo eavEntityStoreRepo;

    @Autowired
    private CouponFeature couponFeature;

    @Autowired
    CouponUserLogRepo couponUserLogRepo;

    @Autowired
    CouponRepo couponRepo;

    @Autowired
    ActivityRepo activityRepo;


    @Transactional
    public OrderPlacementBySystemDTO placeOrderBySystem(OrderBySystem orderBySystemDTO){
        SalesOrder salesOrder = new SalesOrder();

        // Fetch the current increment ID but do not increment it yet
        Integer incrementId = getCurrentOrderIncrementId();
        salesOrder.setIncrementId(incrementId);  // Use the current increment ID

        Double subtotalWithDiscount = orderBySystemDTO.getSubtotal();
        Double discountAmount = 0.0;
        ApplyCouponResponseDTO couponResponse = null;
        Integer paymentMethodId = 1;
        Double grandTotal = 0.0;

        // Apply coupon if present
        if (orderBySystemDTO.getCouponCode() != null && !orderBySystemDTO.getCouponCode().isEmpty()) {
            ApplyCouponByAdminDTO applyCouponByAdminDTO = new ApplyCouponByAdminDTO();
            applyCouponByAdminDTO.setCouponCode(orderBySystemDTO.getCouponCode());
            applyCouponByAdminDTO.setSubtotal(orderBySystemDTO.getSubtotal());
            applyCouponByAdminDTO.setTaxAmount(orderBySystemDTO.getTaxAmount());

            couponResponse = couponFeature.applyCouponByAdmin(applyCouponByAdminDTO);
            grandTotal = orderBySystemDTO.getSubtotal() + orderBySystemDTO.getTaxAmount();

            discountAmount = couponResponse.getSubtotal() + couponResponse.getTaxAmount() - couponResponse.getGrandTotal();
            subtotalWithDiscount = couponResponse.getSubtotal() - discountAmount;
            salesOrder.setSubtotal(couponResponse.getSubtotal());
            salesOrder.setSubtotalWithDiscount(subtotalWithDiscount);
            salesOrder.setIsCouponApplied(true);
        } else {
            grandTotal = orderBySystemDTO.getSubtotal() + orderBySystemDTO.getTaxAmount();
            salesOrder.setIsCouponApplied(false);
        }

        // Generate the invoice increment ID and update it
        Long lastInvoiceIncrementId = eavEntityStoreRepo.findLastInvoiceIncrementId();
        Integer invoiceIncrementId = (lastInvoiceIncrementId != null) ? lastInvoiceIncrementId.intValue() + 1 : 1;
        eavEntityStoreRepo.updateLastInvoiceIncrementId(invoiceIncrementId.longValue());

        salesOrder.setGrandTotal(grandTotal);
        salesOrder.setOrderAmount(grandTotal);
        salesOrder.setCustomerId(orderBySystemDTO.getCustomerId());
        salesOrder.setShippingAddress(orderBySystemDTO.getShippingAddress());
        salesOrder.setBillingAddress(orderBySystemDTO.getBillingAddress());
        salesOrder.setCheckoutMethod(orderBySystemDTO.getCheckoutMethod());
        salesOrder.setPaymentMethodId(paymentMethodId);
        salesOrder.setSubtotal(orderBySystemDTO.getSubtotal());
        salesOrder.setCustomerEmail(orderBySystemDTO.getCustomerEmail());
        salesOrder.setCustomerFullName(orderBySystemDTO.getCustomerFullName());
        salesOrder.setCustomerPhone(orderBySystemDTO.getCustomerPhone());
        salesOrder.setIsActive(true);
        salesOrder.setIsMultiShipping(false);
        salesOrder.setOrderStatusId(1);

        // Save the sales order first, using the current increment ID
        SalesOrder savedSalesOrder = salesOrderRepo.save(salesOrder);

        // Now increment the increment ID for the next order
        updateNextOrderIncrementId(incrementId + 1);

        // Save sales order payment
        SalesOrderPayment salesOrderPayment = new SalesOrderPayment();
        salesOrderPayment.setOrderId(savedSalesOrder.getId().intValue());
        salesOrderPayment.setCustomerId(savedSalesOrder.getCustomerId());
        salesOrderPayment.setPaymentStatus(7);  // 7 - Pending
        salesOrderPayment.setPaymentMethod(paymentMethodId);
        salesOrderPayment.setTotalAmount(orderBySystemDTO.getGrandTotal());
        salesOrderPayment.setPaidAmount(0.00);
        salesOrderPayment.setDueAmount(orderBySystemDTO.getGrandTotal());
        salesOrderPayment.setIsRefundInitiated(false);
        salesOrderPayment.setIsOrderActive(true);

        salesOrderPaymentRepo.save(salesOrderPayment);

        // Save sales order items and invoice items
        List<SalesOrderItem> salesOrderItemList = new ArrayList<>();
        List<InvoiceItemV2> salesInvoiceItemList = new ArrayList<>();
        for (OrderBySystem.OrderItemDTO orderItemDTO : orderBySystemDTO.getOrderItems()) {
            SalesOrderItem salesOrderItem = new SalesOrderItem();
            salesOrderItem.setOrderId(savedSalesOrder.getIncrementId());
            salesOrderItem.setProductId(orderItemDTO.getProductId());
            salesOrderItem.setPrice(orderItemDTO.getPrice());
            salesOrderItem.setQty(orderItemDTO.getQty());
            salesOrderItem.setRowTotal(orderItemDTO.getRowTotal());
            salesOrderItemList.add(salesOrderItem);

            InvoiceItemV2 salesInvoiceItem = new InvoiceItemV2();
            salesInvoiceItem.setProductId(orderItemDTO.getProductId());
            salesInvoiceItem.setPrice(orderItemDTO.getPrice());
            salesInvoiceItem.setQty(orderItemDTO.getQty());
            salesInvoiceItem.setRowTotal(orderItemDTO.getRowTotal());
            salesInvoiceItem.setRowTotalTaxAmount(orderItemDTO.getRowTotalTaxAmount());
            salesInvoiceItem.setTaxAmount(orderItemDTO.getTaxAmount());
            salesInvoiceItem.setTaxPercent(orderItemDTO.getTaxPercent());
            salesInvoiceItem.setPriceInclTax(orderItemDTO.getPriceInclTax());
            salesInvoiceItem.setSku(orderItemDTO.getSku());
            salesInvoiceItemList.add(salesInvoiceItem);
        }
        salesOrderItemRepo.saveAll(salesOrderItemList);

        // Save the sales invoice
        InvoiceV2 salesInvoice = new InvoiceV2();
        salesInvoice.setIsActive(true);
        salesInvoice.setIsMultiShipping(false);
        salesInvoice.setOriginalOrderId(savedSalesOrder.getId().intValue());
        salesInvoice.setCurrencyCode("INR");
        salesInvoice.setGrandTotal(grandTotal);
        salesInvoice.setCheckoutMethod(orderBySystemDTO.getCheckoutMethod());
        salesInvoice.setTaxAmount(orderBySystemDTO.getTaxAmount());
        salesInvoice.setSubtotal(orderBySystemDTO.getSubtotal());
        salesInvoice.setCustomerId(orderBySystemDTO.getCustomerId());
        salesInvoice.setCustomerEmail(orderBySystemDTO.getCustomerEmail());
        salesInvoice.setCustomeFullname(orderBySystemDTO.getCustomerFullName());
        salesInvoice.setReserveOrderId(savedSalesOrder.getIncrementId().toString());
        salesInvoice.setIsCouponApplied(orderBySystemDTO.getIsCouponApplied());

        if (orderBySystemDTO.getCouponCode() != null && !orderBySystemDTO.getCouponCode().isEmpty()) {
            salesInvoice.setCouponCode(orderBySystemDTO.getCouponCode());
            salesInvoice.setDiscountedGrandTotal(couponResponse.getGrandTotal() - couponResponse.getDiscountAmount());
            salesInvoice.setSubtotalWithDiscount(subtotalWithDiscount);
            salesInvoice.setDiscountedTaxAmount(couponResponse.getTaxAmount());
            salesInvoice.setCouponDiscountAmount(discountAmount);
        }

        invoiceRepo.save(salesInvoice);
        salesInvoice.setInvoiceV2(salesInvoiceItemList);
        salesOrderItemRepo.saveAll(salesOrderItemList);

        OrderPlacementBySystemDTO orderPlacementBySystemDTO = new OrderPlacementBySystemDTO();
        orderPlacementBySystemDTO.setOrderId(savedSalesOrder.getIncrementId().intValue());

        return orderPlacementBySystemDTO;
    }

    private Integer getCurrentOrderIncrementId() {
        Long lastOrderIncrementId = eavEntityStoreRepo.findLastOrderIncrementId();
        return (lastOrderIncrementId != null) ? lastOrderIncrementId.intValue() : 1;
    }


    private void updateNextOrderIncrementId(Integer nextIncrementId) {
        eavEntityStoreRepo.updateLastOrderIncrementId(nextIncrementId.longValue());
    }


    @Transactional
    public OrderPlacementByCartIdDTO placeOrder(OrderByCartIdDTO orderByCartIdDTO) {

            Cart cart = cartRepo.findById(Long.valueOf(orderByCartIdDTO.getCartId())).orElseThrow();

            List<SalesOrderItem> salesOrderItemList = new ArrayList<>();
            List<InvoiceItemV2> salesInvoiceItemList = new ArrayList<>();

            for(CartItem cartItem: cart.getCartItems()){
                SalesOrderItem salesOrderItem = new SalesOrderItem();
                salesOrderItem.setProductId(cartItem.getProductId());
                salesOrderItem.setOrderId(Integer.parseInt(cart.getReserveOrderId()));
                salesOrderItem.setPrice(cartItem.getPrice());
                salesOrderItem.setRowTotal(cartItem.getRowTotal());
                salesOrderItem.setQty(cartItem.getQty());
                salesOrderItemList.add(salesOrderItem);

                /**
                 * Adding Invoice Item
                 */
                InvoiceItemV2 salesInvoiceItem = new InvoiceItemV2();
                salesInvoiceItem.setRowTotal(cartItem.getRowTotal());
                salesInvoiceItem.setIsSubItems(false);
                salesInvoiceItem.setHasSubItems(true);
                salesInvoiceItem.setPrice(cartItem.getPrice());
                salesInvoiceItem.setQty(cartItem.getQty());
                salesInvoiceItem.setProductId(cartItem.getProductId());
                salesInvoiceItem.setParentItemId(cartItem.getProductId());
                salesInvoiceItem.setSku(cartItem.getSku()); // Parent Configurable Product Id
                salesInvoiceItem.setTaxPercent(cartItem.getTaxPercent());
                salesInvoiceItem.setRowTotalTaxAmount(cartItem.getRowTotalTaxAmount());
                salesInvoiceItem.setTaxAmount(cartItem.getTaxAmount());
                salesInvoiceItem.setPriceInclTax(cartItem.getPriceInclTax());
                salesInvoiceItemList.add(salesInvoiceItem);

                /**
                 * =================================
                 */

            }

            SalesOrder salesOrder = new SalesOrder();
            salesOrder.setCustomerEmail(orderByCartIdDTO.getCustomerEmail());
            salesOrder.setCustomerFullName(orderByCartIdDTO.getCustomerFullName());
            salesOrder.setCustomerPhone(orderByCartIdDTO.getCustomerPhone());
            salesOrder.setCustomerId(cart.getCustomerId());
            salesOrder.setIncrementId(Integer.parseInt(cart.getReserveOrderId()));
            salesOrder.setIsActive(true);
            salesOrder.setOrderAmount(cart.getGrandTotal());
            salesOrder.setPaymentMethodId(1);
            salesOrder.setBillingAddress(orderByCartIdDTO.getBillingAddress());
            salesOrder.setShippingAddress(orderByCartIdDTO.getShippingAddress());
            salesOrder.setSalesOrderItem(salesOrderItemList);
            salesOrder.setGrandTotal(cart.getGrandTotal());
            salesOrder.setSubtotal(cart.getSubtotal());
            salesOrder.setSubtotalWithDiscount(cart.getSubtotalWithDiscount());
            salesOrder.setIsMultiShipping(false);
            salesOrder.setOrderStatusId(1);
            Long salesOrderId = salesOrderRepo.save(salesOrder).getId();

            SalesOrderPayment salesOrderPayment = new SalesOrderPayment();
            salesOrderPayment.setOrderId(salesOrderId.intValue());
            salesOrderPayment.setCustomerId(cart.getCustomerId());
            // 7 - Pending | 8 - Received | 9 - Refund Initiated | 10 - Refunded | 11 - Failed
            salesOrderPayment.setPaymentStatus(7);
            salesOrderPayment.setPaymentMethod(2);
            salesOrderPayment.setTotalAmount(cart.getGrandTotal());
            salesOrderPayment.setPaidAmount(0.00);
            salesOrderPayment.setDueAmount(cart.getGrandTotal());
            salesOrderPayment.setIsRefundInitiated(false);
            salesOrderPayment.setIsOrderActive(true);


            /* Invoice Generation on Order Placement*/
            InvoiceV2 salesInvoice = new InvoiceV2();
            salesInvoice.setIsActive(true);
            salesInvoice.setIsMultiShipping(false);
            salesInvoice.setItemsCount(cart.getItemsCount());
            salesInvoice.setItemsQty(cart.getItemsQty());
            salesInvoice.setOriginalOrderId(cart.getOriginalOrderId());
            salesInvoice.setCurrencyCode("INR");
            salesInvoice.setGrandTotal(cart.getGrandTotal());
            salesInvoice.setCheckoutMethod("LOGGED_CUSTOMER");
            salesInvoice.setTaxAmount(cart.getTaxAmount());
            salesInvoice.setCouponCode(cart.getCouponCode());
            salesInvoice.setSubtotal(cart.getSubtotal());
            salesInvoice.setIsCouponApplied(cart.getIsCouponApplied());
            salesInvoice.setCouponDiscountAmount(cart.getCouponDiscountAmount());
            salesInvoice.setDiscountedGrandTotal(cart.getDiscountedGrandTotal());
            salesInvoice.setDiscountedTaxAmount(cart.getDiscountedTaxAmount());
            salesInvoice.setTotalDiscountAmount(cart.getTotalDiscountAmount());
            salesInvoice.setOfferDiscountAmount(cart.getOfferDiscountAmount());
            salesInvoice.setOfferCode(cart.getOfferCode());
            salesInvoice.setSubtotalWithDiscount(cart.getSubtotalWithDiscount());
            salesInvoice.setCustomerId(cart.getCustomerId());
            salesInvoice.setCustomerGroupId(cart.getCustomerGroupId());
            salesInvoice.setCustomerEmail(orderByCartIdDTO.getCustomerEmail());
            salesInvoice.setCustomeFullname(orderByCartIdDTO.getCustomerFullName());
            salesInvoice.setReserveOrderId(cart.getReserveOrderId());
            salesInvoice.setInvoiceV2(salesInvoiceItemList);

            if(cart.getIsCouponApplied()){

                CouponUserLog couponUserLog = new CouponUserLog();
                couponUserLog.setCouponCode(cart.getCouponCode());
                couponUserLog.setCustomerId(cart.getCustomerId());
                couponUserLogRepo.save(couponUserLog);

                Coupon coupon = couponRepo.findByCouponCode(cart.getCouponCode());
                if (coupon == null) {
                    throw new IllegalArgumentException("Coupon code not found: " + cart.getCouponCode());
                }
                coupon.setCouponUsedQty(coupon.getCouponUsedQty() + 1);
                couponRepo.save(coupon);

            }

            salesOrderPaymentRepo.save(salesOrderPayment);
            invoiceRepo.save(salesInvoice);
            cartRepo.deleteById(cart.getId());

            //todo send email to customer email, admin, email
            //todo send sms to customer phone

            OrderPlacementByCartIdDTO orderPlacementByCartIdDTO = new OrderPlacementByCartIdDTO();
            orderPlacementByCartIdDTO.setOrderId(salesOrder.getIncrementId());

            return orderPlacementByCartIdDTO;


    }

    public Page<SalesOrder> findOrdersByCustomerId(Pageable pageable, Integer customerId){
        return salesOrderRepo.findOrdersByCustomerId(pageable, customerId);
    }

    public SalesOrder findOrdersByCustomerIdAndOrderId(Integer customerId, Integer orderId){
        return salesOrderRepo.findOrdersByCustomerIdAndOrderId(customerId, orderId);
    }

    public SalesOrder findById(Long orderId){
        return salesOrderRepo.findById(orderId).orElseThrow(() ->
                new PartialContentException("Partial content error: Order with ID '" + orderId + "' could not be fetched.")
        );
    }

    @Transactional
    public void assignOrderToAgent(Integer userId, Long orderId){
        SalesOrder salesOrder = salesOrderRepo.findById(orderId).orElseThrow();

        Activity activity = new Activity();
        activity.setActivityType("ORDER_AGENT_ASSIGNED");
        activity.setModelEntityType("ORDER");
        activity.setEntityId(orderId.intValue());
        activity.setUserId(userId);
        activity.setActivityLog("Agent assigned for order Id " + salesOrder.getIncrementId());
        activityRepo.save(activity);

    }


    @Transactional
    public void orderStatusUpdate(Long orderId, OrderStatusUpdateDTO orderStatusUpdateDTO) {
        SalesOrder salesOrder = salesOrderRepo.findById(orderId).orElseThrow();

        // Update order status
        salesOrder.setOrderStatusId(orderStatusUpdateDTO.getStatus());
        salesOrderRepo.save(salesOrder);

        Activity activity = new Activity();
        activity.setActivityType("ORDER_STATUS_UPDATE");
        activity.setModelEntityType("ORDER");
        activity.setEntityId(orderId.intValue());
        //todo add user by fetching the userId from the loggedIn session from the header passed by zuul server
        //activity.setUserId();
        activity.setComment(orderStatusUpdateDTO.getComment());
        activity.setActivityLog("Status changed for order Id " + salesOrder.getIncrementId());
        activityRepo.save(activity);

        // Prepare email data
        SendEmailDTO.VariableData variableData = new SendEmailDTO.VariableData();
        variableData.setCustomerName(salesOrder.getCustomerFullName());
        variableData.setIncrementId(salesOrder.getIncrementId().toString());
        variableData.setLink("http://bit.ly/p1we1");  // Example link for order tracking

        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setVariableData(variableData);
        sendEmailDTO.setEmail(salesOrder.getCustomerEmail());
        switch (orderStatusUpdateDTO.getStatus()) {
            case 1:
                sendEmailDTO.setTemplate("order-confirmed");
                sendEmailDTO.setSubjectLine("Your order has been confirmed");
                break;
            case 4:
                sendEmailDTO.setTemplate("order-shipped");
                sendEmailDTO.setSubjectLine("Your order is shipped");
                break;
            case 5:
                sendEmailDTO.setTemplate("order-cancelled");
                sendEmailDTO.setSubjectLine("Your order has been cancelled");
                break;
            case 6:
                sendEmailDTO.setTemplate("order-delivered");
                sendEmailDTO.setSubjectLine("Your order is successfully completed");
                break;
            case 14:
                sendEmailDTO.setTemplate("out-for-delivery-comms");
                sendEmailDTO.setSubjectLine("Your order is out for delivery");
                break;
            default:
                return;
        }

        // Send email
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        PostRequest postRequest = new PostRequest();
        postRequest.setReqUrl("http://127.0.0.1:8081/send");
        postRequest.setReqBody(sendEmailDTO);
        postRequest.setReqHeaders(headers);
        IServiceCommand serviceCommand = new PostServiceCommand(postRequest);
        ServiceInvoker serviceInvoker = new ServiceInvoker(serviceCommand);
        serviceInvoker.ExecuteRequest();
    }


    public Page<SalesOrder> findAll(Pageable pageable){
        return salesOrderRepo.findAll(pageable);
    }

    public Page<SalesOrder> findEmptyOrders(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

}
