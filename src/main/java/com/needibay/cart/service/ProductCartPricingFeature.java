package com.needibay.cart.service;

import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.ProductDynamicPricing;
import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.entity.cart.CartItem;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.repository.cart.CartItemRepo;
import com.needibay.cart.repository.cart.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCartPricingFeature implements Feature {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    public Double getSubtotal(Integer productId, Integer qty) {

        Product product = productRepo.findById(productId).orElseThrow();
        Double basePriceWithCommission = product.getBasePrice();
        List<ProductDynamicPricing> productDynamicPricing = product.getProductDynamicPricing();
        if(productDynamicPricing != null){
            if(productDynamicPricing.size() > 0) {
                for (ProductDynamicPricing obj : productDynamicPricing
                ) {
                    if(qty >= obj.getMinQty() && qty <= obj.getMaxQty()){
                        basePriceWithCommission = obj.getPrice();
                    }
                    if(qty > productDynamicPricing.get(productDynamicPricing.size() -1).getMaxQty()){
                        basePriceWithCommission = productDynamicPricing.get(productDynamicPricing.size() -1).getPrice();
                    }

                }
            }
        }

        Double subtotal = basePriceWithCommission * qty;

        String commissionType = product.getBaseCommissionType().toLowerCase();

        if(commissionType.equals("percent")){

            Double commission = product.getBaseCommission();
            subtotal += (subtotal * commission) / 100;
        }

        if(commissionType.equals("flat")){

            Double commission = product.getBaseCommission();

            subtotal += commission;
        }

        return subtotal;

    }

    public Double getBasePriceWithCommission(Integer productId, Integer... optionalParams) {
        Integer optionalParam = (optionalParams.length > 0) ? optionalParams[0] : null;
        Product product = productRepo.findById(productId).orElseThrow();
        Double basePriceWithCommission = product.getBasePrice();

        List<ProductDynamicPricing> productDynamicPricing = product.getProductDynamicPricing();
        if(productDynamicPricing != null){
            if(productDynamicPricing.size() > 0) {
                for (ProductDynamicPricing obj : productDynamicPricing
                     ) {
                    if(optionalParam >= obj.getMinQty() && optionalParam <= obj.getMaxQty()){
                        basePriceWithCommission = obj.getPrice();
                    }
                    if(optionalParam > productDynamicPricing.get(productDynamicPricing.size() -1).getMaxQty()){
                        basePriceWithCommission = productDynamicPricing.get(productDynamicPricing.size() -1).getPrice();
                    }

                }
            }
        }


        String commissionType = product.getBaseCommissionType().toLowerCase();

        if(commissionType.equals("percent")){

            Double commission = product.getBaseCommission();
            basePriceWithCommission += (basePriceWithCommission * commission) / 100;
        }

        if(commissionType.equals("flat")){

            Double commission = product.getBaseCommission();

            basePriceWithCommission += commission;
        }

        return basePriceWithCommission;

    }

    public Double updateCartTotal(Integer cartId) {

        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);

        Double cartItemsPriceInclusiveTax = 0.00;

        for (CartItem cartItem: cartItems){
            cartItemsPriceInclusiveTax += cartItem.getRowTotalInclTax();
        }

        return cartItemsPriceInclusiveTax;

    }


    public Double updateCartSubTotal(Integer cartId) {

        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);

        Double cartItemsPriceInclusiveTax = 0.00;

        for (CartItem cartItem: cartItems){
            cartItemsPriceInclusiveTax += cartItem.getRowTotal();
        }

        return cartItemsPriceInclusiveTax;

    }

    public Double updateCartTaxTotal(Integer cartId) {

        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);

        Double cartItemsPriceInclusiveTax = 0.00;

        for (CartItem cartItem: cartItems){
            cartItemsPriceInclusiveTax += cartItem.getRowTotalTaxAmount();
        }

        return cartItemsPriceInclusiveTax;

    }

    public Double updateDiscountedCartTotal(Integer cartId) {

        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);

        Double cartItemsPriceInclusiveTax = 0.00;

        for (CartItem cartItem: cartItems){
            cartItemsPriceInclusiveTax += cartItem.getRowTotalInclTax();
        }

        return cartItemsPriceInclusiveTax;

    }





}
