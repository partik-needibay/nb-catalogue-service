package com.needibay.cart.service.cart;

import com.needibay.cart.dto.coupon.ApplyCouponDTO;
import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.entity.cart.CartItem;
import com.needibay.cart.entity.coupon.Coupon;
import com.needibay.cart.exception.Coupon.CouponNotApplicableException;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.repository.cart.CartItemRepo;
import com.needibay.cart.repository.cart.CartRepo;
import com.needibay.cart.repository.coupon.CouponRepo;
import com.needibay.cart.service.Feature;
import com.needibay.cart.service.ProductCartPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Component
public class CartItemFeature implements Feature {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductCartPricingService productCartPricingService;

    @Autowired
    CouponRepo couponRepo;



    /*
     * Method add items in the existing cart
     * Conditional - Item already exist -> False
     * */
    @Transactional
    public Cart addCartItemById(Cart cart, Long id) {

        Double cartTotalTaxAmount = 0.00;
        Double cartTotalTaxPercent = 0.00;

        Cart existingCart = cartRepo.findById(id).orElse(null);

        List<CartItem> existingCartItems = existingCart.getCartItems();

        for (CartItem obj: cart.getCartItems()){

            Optional<CartItem> cartItem = existingCart.getCartItems().stream().filter(item -> item.getProductId().equals(obj.getProductId())).findFirst();

            if(!cartItem.isPresent()){
                Product productItem = productRepo.findById(obj.getProductId()).orElseThrow();
                Integer quantity = obj.getQty();
                obj.setRowTotalQty(quantity);
                if(obj.getIsSampleQty() == false || obj.getIsSampleQty() == null) {
                    if(productItem.getMinOrderQty() != "" && productItem.getMinOrderQty() != null) {
                        quantity = obj.getQty() *  Integer.parseInt(productItem.getMinOrderQty());
                        obj.setMinOrderQty(Integer.parseInt(productItem.getMinOrderQty()));
                        obj.setRowTotalQty(obj.getQty() * Integer.parseInt(productItem.getMinOrderQty()));
                    }
                }

                Double rowTotal = productCartPricingService.getFeature().getProductCartPricingFeature().getSubtotal(obj.getProductId(), quantity);
                Double rowTotalTaxAmount = (rowTotal * productItem.getTaxPercent())/100;
                Double basePriceWithCommission = productCartPricingService.getFeature().getProductCartPricingFeature().getBasePriceWithCommission(obj.getProductId(), obj.getRowTotalQty());
                Double unitTaxAmountPerProduct =  (basePriceWithCommission * productItem.getTaxPercent())/100;
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
                obj.setRowTotalInclTax(rowTotal + rowTotalTaxAmount);
                if(productItem.getIsVariant()){
                    obj.setParentItemId(productItem.getParentConfigProductId());
                }
                obj.setTaxPercent(productItem.getTaxPercent());
                obj.setRowTotalTaxAmount(rowTotalTaxAmount);
                obj.setTaxAmount(unitTaxAmountPerProduct);
                obj.setPriceInclTax(basePriceWithCommission + unitTaxAmountPerProduct);
                cartTotalTaxAmount += rowTotalTaxAmount;
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

        if(existingCart.getIsCouponApplied() != null && existingCart.getIsCouponApplied()){
            ApplyCouponDTO applyCouponDTO = new ApplyCouponDTO();
            applyCouponDTO.setCartId(existingCart.getId());
            applyCouponDTO.setCouponCode(existingCart.getCouponCode());
            this.applyCoupon(applyCouponDTO);
        }

        return existingCart;
    }

    /*
     * Method add items in the existing cart
     *
     * */
    @Transactional
    public Cart removeCartItemById(Cart cart, Long id) {

        Cart existingCart = cartRepo.findById(id).orElse(null);

        List<CartItem> existingCartItems = existingCart.getCartItems();

        for (CartItem obj: cart.getCartItems()){

            Optional<CartItem> cartItem = existingCart.getCartItems().stream().filter(item -> item.getProductId().equals(obj.getProductId())).findFirst();

            if(cartItem.isPresent()){
                existingCartItems.removeIf(child -> child.getProductId().equals(obj.getProductId()));
            }
        }
        Double updatedGrandTotal = productCartPricingService.getFeature().getProductCartPricingFeature().updateCartTotal(id.intValue());

        existingCart.setGrandTotal(updatedGrandTotal);



        cartRepo.save(existingCart);

        //todo need to check edge cases
        if(existingCart.getIsCouponApplied() != null && existingCart.getIsCouponApplied()){
            ApplyCouponDTO applyCouponDTO = new ApplyCouponDTO();
            applyCouponDTO.setCartId(existingCart.getId());
            applyCouponDTO.setCouponCode(existingCart.getCouponCode());
            if(existingCartItems.size() == 0){
                this.removeAppliedCoupon(applyCouponDTO);
            }else{
                this.applyCoupon(applyCouponDTO);
            }
        }

        return existingCart;

    }

    public List<Coupon> findAll() {
        return couponRepo.findAll();
    }

    @Transactional
    public Cart removeAppliedCoupon(ApplyCouponDTO applyCouponDTO){

        Cart existingCart = cartRepo.findById(applyCouponDTO.getCartId()).orElse(null);
        Coupon appliedCoupon = couponRepo.findByCouponCode(applyCouponDTO.getCouponCode());

        existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - appliedCoupon.getMaxDiscountAmt());
        existingCart.setCouponDiscountAmount(appliedCoupon.getMaxDiscountAmt());

        existingCart.setCouponCode(appliedCoupon.getCouponCode());

        if(existingCart.getCartItems().size() > 0) {

            for (CartItem obj: existingCart.getCartItems()){

                obj.setRowTotalWithDiscount(null);

                obj.setDiscountAmount(null);

                obj.setDiscountPercent(null);

                obj.setIsCouponApplied(true);

                obj.setRowTotalDiscountedTaxAmount(null);
                cartItemRepo.save(obj);

            }
        }

        existingCart.setIsCouponApplied(false);

        existingCart.setDiscountedGrandTotal(null);

        existingCart.setDiscountedTaxAmount(null);

        existingCart.setSubtotalWithDiscount(null);

        cartRepo.save(existingCart);

        return existingCart;

    }

    @Transactional
    public Cart  applyCoupon(ApplyCouponDTO applyCouponDTO){
        Double applicableDiscountAmount = 0.00;

        Date today = new Date();

        Cart existingCart = cartRepo.findById(applyCouponDTO.getCartId()).orElse(null);

        Double cartSubtotal = existingCart.getSubtotal();

        Coupon appliedCoupon = couponRepo.findByCouponCode(applyCouponDTO.getCouponCode());

        /*if(existingCart.getIsCouponApplied() == true){
            throw new CouponNotApplicableException("Coupon has already applied");
        }*/

        if(!appliedCoupon.getIsActive()){
            throw new CouponNotApplicableException("Minimum Cart should be equal or greater than " + cartSubtotal);
        }

        if(appliedCoupon.getMinOrderPrice() > cartSubtotal){
            throw new CouponNotApplicableException("Minimum Cart should be equal or greater than " + appliedCoupon.getMinOrderPrice());
        }

        //Check if coupon has expired
        if(appliedCoupon.getExpiresAt().before(new Date()) || appliedCoupon.getStartsAt().after(new Date())){
            throw new CouponNotApplicableException("Coupon has expired.");
        }

        if(appliedCoupon.getCouponUserLogs().stream().count() == appliedCoupon.getMaxUsableQuantity()){
            throw new CouponNotApplicableException("Coupon usage limit has reached");
        }

        List<CartItem> existingCartItems = existingCart.getCartItems();

        if(appliedCoupon.getCouponType().equals("PERCENT")){
            Double discountAmount = existingCart.getSubtotal() * appliedCoupon.getPercent()/100;

            if(discountAmount > appliedCoupon.getMaxDiscountAmt()){
                existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - appliedCoupon.getMaxDiscountAmt());
                existingCart.setCouponDiscountAmount(appliedCoupon.getMaxDiscountAmt());
                applicableDiscountAmount = appliedCoupon.getMaxDiscountAmt();

            }else{
                existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - discountAmount);
                existingCart.setCouponDiscountAmount(discountAmount);
                applicableDiscountAmount = discountAmount;
            }

        }

        if(appliedCoupon.getCouponType().equals("FLAT")){
            if(appliedCoupon.getAmount() > appliedCoupon.getMaxDiscountAmt()){
                existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - appliedCoupon.getMaxDiscountAmt());
                existingCart.setCouponDiscountAmount(appliedCoupon.getMaxDiscountAmt());
                applicableDiscountAmount = appliedCoupon.getMaxDiscountAmt();

            }else{
                existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - appliedCoupon.getAmount());
                existingCart.setCouponDiscountAmount(appliedCoupon.getAmount());
                applicableDiscountAmount = appliedCoupon.getAmount();
            }

        }
        Map<String, Double> updatedCartItemDetailAfterDiscount = this.applyDiscountOnCartItems(existingCartItems, appliedCoupon, existingCart, applicableDiscountAmount);

        existingCart.setCouponCode(appliedCoupon.getCouponCode());

        existingCart.setIsCouponApplied(true);

        existingCart.setDiscountedGrandTotal(updatedCartItemDetailAfterDiscount.get("totalTaxAmountAfterDiscount") + updatedCartItemDetailAfterDiscount.get("subTotalAfterDiscount"));

        existingCart.setDiscountedTaxAmount(updatedCartItemDetailAfterDiscount.get("totalTaxAmountAfterDiscount"));

        existingCart.setSubtotalWithDiscount(updatedCartItemDetailAfterDiscount.get("subTotalAfterDiscount"));

        cartRepo.save(existingCart);

        return existingCart;
    }

    private Map<String, Double> applyDiscountOnCartItems(List<CartItem> cartItems, Coupon appliedCoupon, Cart existingCart, Double applicableDiscountAmount) {

        try {

            Map<String, Double> updatedCartItemData = new HashMap<>();

            Double totalTaxAmountAfterDiscount = 0.00;
            Double subTotalAfterDiscount = 0.00;

            if(appliedCoupon.getCouponType().equals("PERCENT")){
                for (CartItem obj: cartItems){

                    /*Double rowTotalWithDiscount = obj.getRowTotal() - (obj.getRowTotal() * appliedCoupon.getPercent()/100);

                    if(rowTotalWithDiscount > appliedCoupon.getMaxDiscountAmt()){
                        existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - appliedCoupon.getMaxDiscountAmt());
                        existingCart.setCouponDiscountAmount(appliedCoupon.getMaxDiscountAmt());

                    }else{
                        existingCart.setSubtotalWithDiscount(existingCart.getSubtotal() - discountAmount);
                        existingCart.setCouponDiscountAmount(discountAmount);
                    }*/

                    Double objItemWeightage = obj.getRowTotal()/existingCart.getSubtotal();
                    Double discountAmount = applicableDiscountAmount * objItemWeightage;
                    Double rowTotalWithDiscount = obj.getRowTotal() - discountAmount;



                    Double rowTotalTaxAmount = rowTotalWithDiscount * obj.getTaxPercent()/100;

                    obj.setRowTotalWithDiscount(rowTotalWithDiscount);

                    obj.setDiscountAmount(rowTotalWithDiscount);

                    obj.setDiscountPercent(appliedCoupon.getPercent());

                    obj.setIsCouponApplied(true);

                    obj.setRowTotalDiscountedTaxAmount(rowTotalWithDiscount * obj.getTaxPercent()/100);
                    //todo replace above with rowTotalDiscountedTaxAmount()

                    totalTaxAmountAfterDiscount += rowTotalTaxAmount;

                    subTotalAfterDiscount += rowTotalWithDiscount;

                    cartItemRepo.save(obj);



                }

            }
            if(appliedCoupon.getCouponType().equals("FLAT")) {

                for (CartItem obj: cartItems){

                    Double objItemWeightage = obj.getRowTotal()/existingCart.getSubtotal();
                    Double discountAmount = appliedCoupon.getAmount() * objItemWeightage;
                    Double rowTotalWithDiscount = obj.getRowTotal() - discountAmount;
                    Double rowTotalTaxAmount = rowTotalWithDiscount * obj.getTaxPercent()/100;

                    obj.setRowTotalWithDiscount(rowTotalWithDiscount);
                    obj.setDiscountAmount(discountAmount);
                    obj.setIsCouponApplied(true);
                    obj.setRowTotalTaxAmount(rowTotalWithDiscount * obj.getTaxPercent()/100);
                    //todo replace above with rowTotalDiscountedTaxAmount()
                    totalTaxAmountAfterDiscount += rowTotalTaxAmount;
                    subTotalAfterDiscount += rowTotalWithDiscount;
                    cartItemRepo.save(obj);


                }
            }
            updatedCartItemData.put("totalTaxAmountAfterDiscount", totalTaxAmountAfterDiscount);
            updatedCartItemData.put("subTotalAfterDiscount", subTotalAfterDiscount);

            return updatedCartItemData;

        }catch (Exception e){
            throw new CouponNotApplicableException("Coupon couldn't apply!");
        }

    }

    @Transactional
    public void deleteCartItemsByCartId(Long cartId){
        cartItemRepo.deleteCartItemsByCartId(cartId);
        cartRepo.deleteById(cartId);
    }

    public List<Coupon> findAllCoupons() {
        return couponRepo.findAll();
    }

}
