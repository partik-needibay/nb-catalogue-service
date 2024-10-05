package com.needibay.cart.service.coupon;

import com.needibay.cart.dto.coupon.CouponDTO;
import com.needibay.cart.entity.coupon.Coupon;
import com.needibay.cart.exception.Coupon.CouponAlreadyExists;
import com.needibay.cart.repository.coupon.CouponRepo;
import com.needibay.cart.service.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.needibay.cart.response.ApplyCouponResponseDTO;
import com.needibay.cart.exception.Coupon.CouponNotApplicableException;
import com.needibay.cart.dto.coupon.ApplyCouponByAdminDTO;
import com.needibay.cart.dto.coupon.ApplyCouponDTO;
import java.util.Date;

@Service
public class CouponFeature implements Feature {

    @Autowired
    CouponRepo couponRepo;

    @Transactional
    public void saveCoupon(CouponDTO couponDTO){
        try{
            Coupon coupon = couponRepo.findByCouponCode(couponDTO.getCouponCode());

            if(coupon != null){
                throw new CouponAlreadyExists("The Coupon Already Exist!");
            }

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

        try{
            Coupon coupon = new Coupon();
            coupon.setCouponCode(couponDTO.getCouponCode());
            coupon.setCouponType(couponDTO.getCouponType());
            coupon.setAmount(couponDTO.getAmount());
            coupon.setPercent(couponDTO.getPercent());
            coupon.setMinOrderPrice(couponDTO.getMinOrderPrice());
            coupon.setProductId(couponDTO.getProductId());
            coupon.setMaxDiscountAmt(couponDTO.getMaxDiscountAmt());
            coupon.setMaxUsableQuantity(couponDTO.getMaxUsableQuantity());
            coupon.setUsageLimitPerUser(couponDTO.getUsageLimitPerUser());
            coupon.setCouponUsedQty(couponDTO.getCouponUsedQty());
            coupon.setCreatedBy(couponDTO.getCreatedBy());
            coupon.setUpdatedBy(couponDTO.getUpdatedBy());
            coupon.setStartsAt(couponDTO.getStartsAt());
            coupon.setExpiresAt(couponDTO.getExpiresAt());
            coupon.setIsActive(couponDTO.getIsActive());
            couponRepo.save(coupon);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Transactional
    public void updateCoupon(CouponDTO couponDTO){
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponDTO.getCouponCode());
        coupon.setCouponType(couponDTO.getCouponType());
        coupon.setAmount(couponDTO.getAmount());
        coupon.setPercent(couponDTO.getPercent());
        coupon.setMinOrderPrice(couponDTO.getMinOrderPrice());
        coupon.setProductId(couponDTO.getProductId());
        coupon.setMaxDiscountAmt(couponDTO.getMaxDiscountAmt());
        coupon.setMaxUsableQuantity(couponDTO.getMaxUsableQuantity());
        coupon.setUsageLimitPerUser(couponDTO.getUsageLimitPerUser());
        coupon.setCouponUsedQty(couponDTO.getCouponUsedQty());
        coupon.setCreatedBy(couponDTO.getCreatedBy());
        coupon.setUpdatedBy(couponDTO.getUpdatedBy());
        coupon.setStartsAt(couponDTO.getStartsAt());
        coupon.setExpiresAt(couponDTO.getExpiresAt());
        coupon.setIsActive(couponDTO.getIsActive());
        couponRepo.save(coupon);
    }

    @Transactional
    public void disableCoupon(CouponDTO couponDTO){
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponDTO.getCouponCode());
        coupon.setCouponType(couponDTO.getCouponType());
        coupon.setAmount(couponDTO.getAmount());
        coupon.setPercent(couponDTO.getPercent());
        coupon.setMinOrderPrice(couponDTO.getMinOrderPrice());
        coupon.setProductId(couponDTO.getProductId());
        coupon.setMaxDiscountAmt(couponDTO.getMaxDiscountAmt());
        coupon.setMaxUsableQuantity(couponDTO.getMaxUsableQuantity());
        coupon.setUsageLimitPerUser(couponDTO.getUsageLimitPerUser());
        coupon.setCouponUsedQty(couponDTO.getCouponUsedQty());
        coupon.setCreatedBy(couponDTO.getCreatedBy());
        coupon.setUpdatedBy(couponDTO.getUpdatedBy());
        coupon.setStartsAt(couponDTO.getStartsAt());
        coupon.setExpiresAt(couponDTO.getExpiresAt());
        coupon.setIsActive(couponDTO.getIsActive());
        couponRepo.save(coupon);
    }
    @Transactional
    public ApplyCouponResponseDTO applyCouponByAdmin(ApplyCouponByAdminDTO applyCouponByAdminDTO) {
        Coupon coupon = couponRepo.findByCouponCode(applyCouponByAdminDTO.getCouponCode());

        Date today = new Date();
        Double cartSubtotal = applyCouponByAdminDTO.getSubtotal();

        if (coupon.getExpiresAt().before(today) || coupon.getStartsAt().after(today)) {
            throw new CouponNotApplicableException("Coupon has expired or is not yet valid.");
        }
        if (coupon.getCouponUserLogs().stream().count() >= coupon.getMaxUsableQuantity()) {
            throw new CouponNotApplicableException("Coupon usage limit has been reached.");
        }

        if (!coupon.getIsActive()) {
            throw new CouponNotApplicableException("Coupon is inactive.");
        }


        if (coupon.getMinOrderPrice() > cartSubtotal) {
            throw new CouponNotApplicableException("Minimum cart value should be equal to or greater than " + coupon.getMinOrderPrice());
        }



        Double discountAmount = 0.0;

        if (coupon.getCouponType().equals("PERCENT")) {
            discountAmount = applyCouponByAdminDTO.getSubtotal() * coupon.getPercent() / 100;
            if (discountAmount > coupon.getMaxDiscountAmt()) {
                discountAmount = coupon.getMaxDiscountAmt();
            }
        } else if (coupon.getCouponType().equals("FLAT")) {
            discountAmount = coupon.getAmount();
            if (discountAmount > coupon.getMaxDiscountAmt()) {
                discountAmount = coupon.getMaxDiscountAmt();
            }
        }
        Double newGrandTotal = applyCouponByAdminDTO.getSubtotal() + applyCouponByAdminDTO.getTaxAmount() - discountAmount;

        if (coupon.getCouponUserLogs().stream().count() >= coupon.getMaxUsableQuantity()) {
            throw new CouponNotApplicableException("Coupon usage limit has been reached.");
        }

        ApplyCouponResponseDTO responseDTO = new ApplyCouponResponseDTO();
        responseDTO.setGrandTotal(newGrandTotal);
        responseDTO.setSubtotal(applyCouponByAdminDTO.getSubtotal());
        responseDTO.setTaxAmount(applyCouponByAdminDTO.getTaxAmount());
        responseDTO.setDiscountAmount(discountAmount);


        return responseDTO;
    }

    public ApplyCouponResponseDTO removeCouponByAdmin(ApplyCouponByAdminDTO applyCouponByAdminDTO) {
        Double originalSubtotal = applyCouponByAdminDTO.getSubtotal();
        Double originalTaxAmount = applyCouponByAdminDTO.getTaxAmount();

        // Calculate what the subtotal would have been without the discount (should not change)
        Double discountAmount = 0.0;

        // Set values to original amounts
        Double newSubtotal = originalSubtotal;
        Double newGrandTotal = originalSubtotal + originalTaxAmount;

        // Prepare and return the response
        ApplyCouponResponseDTO responseDTO = new ApplyCouponResponseDTO();
        responseDTO.setSubtotal(newSubtotal); // Original subtotal before any discount
        responseDTO.setTaxAmount(originalTaxAmount); // Original tax amount
        responseDTO.setDiscountAmount(0.0); // No discount applied
        responseDTO.setGrandTotal(newGrandTotal); // Grand total should be the sum of the original subtotal and tax

        return responseDTO;
    }
}
