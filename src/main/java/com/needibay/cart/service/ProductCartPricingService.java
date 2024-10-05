package com.needibay.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCartPricingService implements Feature {

    @Autowired
    FeatureProvider featureProvider;

    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
