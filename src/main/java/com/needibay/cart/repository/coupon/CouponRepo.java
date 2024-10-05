package com.needibay.cart.repository.coupon;

import com.needibay.cart.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {

    @Query(value = "select * from nb_coupons where coupon_code = :couponCode", nativeQuery = true)
    public Coupon findByCouponCode(String couponCode);
}
