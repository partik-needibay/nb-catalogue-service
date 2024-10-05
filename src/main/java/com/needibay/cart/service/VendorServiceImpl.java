package com.needibay.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    FeatureProvider featureProvider;

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
