package com.needibay.cart.repository.coupon;

import com.needibay.cart.entity.coupon.CouponUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserLogRepo extends JpaRepository<CouponUserLog, Long> {
}
