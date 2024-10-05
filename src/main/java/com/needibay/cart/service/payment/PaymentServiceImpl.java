package com.needibay.cart.service.payment;

import com.needibay.cart.service.FeatureProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    FeatureProvider featureProvider;

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
