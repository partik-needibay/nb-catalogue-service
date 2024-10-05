package com.needibay.cart.service.coupon;

import com.needibay.cart.service.FeatureProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    FeatureProvider featureProvider;

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
