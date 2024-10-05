package com.needibay.cart.service.cart;

import com.needibay.cart.dto.coupon.ApplyCouponDTO;
import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.entity.cart.CartItem;
import com.needibay.cart.entity.cart.EavEntityStore;
import com.needibay.cart.entity.coupon.Coupon;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.repository.cart.CartItemRepo;
import com.needibay.cart.repository.cart.CartRepo;
import com.needibay.cart.repository.cart.EavEntityStoreRepo;
import com.needibay.cart.repository.coupon.CouponRepo;
import com.needibay.cart.service.FeatureProvider;
import com.needibay.cart.service.ProductCartPricingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerCartService implements CartService {

    @Autowired
    CartRepo cartRepo;


    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    FeatureProvider featureProvider;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductCartPricingService productCartPricingService;

    @Autowired
    EavEntityStoreRepo eavEntityStoreRepo;



    @Override
    @Transactional
    public void save(Cart cart) {
        Double cartGrandTotal = 0.00;
        Double cartTotalTaxAmout = 0.00;
        Double cartSubTotal = 0.00;



        for (CartItem obj: cart.getCartItems()){
                Product productItem = productRepo.findById(obj.getProductId()).orElseThrow();
                Integer quantity = obj.getQty();
                obj.setRowTotalQty(quantity);
                if(productItem.getMinOrderQty() != "" && productItem.getMinOrderQty() != null && obj.getIsSampleQty() == false) {
                    quantity = obj.getQty() *  Integer.parseInt(productItem.getMinOrderQty());
                    obj.setMinOrderQty(Integer.parseInt(productItem.getMinOrderQty()));
                    obj.setRowTotalQty(obj.getQty() * Integer.parseInt(productItem.getMinOrderQty()));
                }
                Double rowTotal = productCartPricingService.getFeature().getProductCartPricingFeature().getSubtotal(obj.getProductId(), quantity);
                Double rowTotalTaxAmount = (rowTotal * productItem.getTaxPercent())/100;
                Double basePriceWithCommission = productCartPricingService.getFeature().getProductCartPricingFeature().getBasePriceWithCommission(obj.getProductId(), obj.getRowTotalQty());
                Double unitTaxAmountPerProduct =  (basePriceWithCommission * productItem.getTaxPercent())/100;
                Double rowTotalIncludingTax = rowTotal + rowTotalTaxAmount;

                obj.setProductId(obj.getProductId());
                obj.setSku(productItem.getSku());
                obj.setRowTotal(rowTotal);
                obj.setQty(obj.getQty());
                obj.setPrice(basePriceWithCommission);
                obj.setRowTotalWithDiscount(rowTotal);
                obj.setIsQtyDecimal(false);
                obj.setProductType(productItem.getProductType());
                obj.setName(productItem.getProductName());
                obj.setRowTotalInclTax(rowTotalIncludingTax);
                if(productItem.getIsVariant()){
                    obj.setParentItemId(productItem.getParentConfigProductId());
                }
                obj.setTaxPercent(productItem.getTaxPercent());
                obj.setRowTotalTaxAmount(rowTotalTaxAmount);
                obj.setTaxAmount(unitTaxAmountPerProduct);
                obj.setPriceInclTax(basePriceWithCommission + unitTaxAmountPerProduct);
                cartGrandTotal += rowTotalIncludingTax;
                cartTotalTaxAmout += rowTotalTaxAmount;
                cartSubTotal += rowTotal;
        }

        Long lastOrderIncrementId = eavEntityStoreRepo.findLastOrderIncrementId();

        Long incrementLastOrderIncrementId = lastOrderIncrementId + 1;

        cart.setReserveOrderId(lastOrderIncrementId.toString());

        eavEntityStoreRepo.updateLastOrderIncrementId(incrementLastOrderIncrementId);
        cart.setGrandTotal(cartGrandTotal);
        cart.setTaxAmount(cartTotalTaxAmout);
        cart.setSubtotal(cartSubTotal);
        cart.setIsActive(true);
        cartRepo.save(cart);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepo.findAll();
    }


    /*
    * Method update existing cart Item
    * */
    @Override
    @Transactional
    public Cart updateById(Cart cart, Long id) {

        Cart existingCart = cartRepo.findById(id).orElse(null);

        for (CartItem obj: cart.getCartItems()) {

            if (obj.getId() != null) {
                Product productItem = productRepo.findById(obj.getProductId()).orElseThrow();
                Integer quantity = obj.getQty();
                obj.setRowTotalQty(quantity);
                // Safely handle null for getIsSampleQty
                Boolean isSampleQty = obj.getIsSampleQty() != null ? obj.getIsSampleQty() : false;

                if (productItem.getMinOrderQty() != "" && productItem.getMinOrderQty() != null && !isSampleQty) {
                    quantity = obj.getQty() * Integer.parseInt(productItem.getMinOrderQty());
                    obj.setMinOrderQty(Integer.parseInt(productItem.getMinOrderQty()));
                    obj.setRowTotalQty(obj.getQty() * Integer.parseInt(productItem.getMinOrderQty()));
                }

                Double rowTotal = productCartPricingService.getFeature().getProductCartPricingFeature().getSubtotal(obj.getProductId(), quantity);
                Double rowTotalTaxAmount = (rowTotal * productItem.getTaxPercent()) / 100;
                Double basePriceWithCommission = productCartPricingService.getFeature().getProductCartPricingFeature().getBasePriceWithCommission(obj.getProductId(), obj.getRowTotalQty());
                Double unitTaxAmountPerProduct = (basePriceWithCommission * productItem.getTaxPercent()) / 100;
                Double rowTotalIncludingTax = rowTotal + rowTotalTaxAmount;

                obj.setCart(existingCart);
                obj.setProductId(obj.getProductId());
                obj.setSku(productItem.getSku());
                obj.setRowTotal(rowTotal);
                obj.setQty(obj.getQty());
                obj.setPrice(basePriceWithCommission);
                obj.setRowTotalWithDiscount(rowTotal);
                obj.setIsQtyDecimal(false);
                obj.setProductType(productItem.getProductType());
                obj.setName(productItem.getProductName());
                obj.setRowTotalInclTax(rowTotalIncludingTax);

                if (productItem.getIsVariant()) {
                    obj.setParentItemId(productItem.getParentConfigProductId());
                }

                obj.setTaxPercent(productItem.getTaxPercent());
                obj.setRowTotalTaxAmount(rowTotalTaxAmount);
                obj.setTaxAmount(unitTaxAmountPerProduct);
                obj.setPriceInclTax(rowTotal);
                cartItemRepo.save(obj);
            }
        }

        Double updatedGrandTotal = productCartPricingService.getFeature().getProductCartPricingFeature().updateCartTotal(id.intValue());
        Double updatedSubTotal = productCartPricingService.getFeature().getProductCartPricingFeature().updateCartSubTotal(id.intValue());
        Double updateCartTaxTotal = productCartPricingService.getFeature().getProductCartPricingFeature().updateCartTaxTotal(id.intValue());

        existingCart.setGrandTotal(updatedGrandTotal);
        existingCart.setTaxAmount(updateCartTaxTotal);
        existingCart.setSubtotal(updatedSubTotal);

        cartRepo.save(existingCart);

        if (existingCart.getIsCouponApplied() != null && existingCart.getIsCouponApplied()) {
            ApplyCouponDTO applyCouponDTO = new ApplyCouponDTO();
            applyCouponDTO.setCartId(existingCart.getId());
            applyCouponDTO.setCouponCode(existingCart.getCouponCode());
            this.getFeature().getCartItemFeature().applyCoupon(applyCouponDTO);
        }

        return existingCart;
    }

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }

    @Override
    public Cart findById(Long id) {
        return cartRepo.findById(id).orElseThrow();
    }

    @Override
    public Cart findByCustomerId(Integer customerId) {
        return cartRepo.findByCustomerId(customerId);
    }



}
