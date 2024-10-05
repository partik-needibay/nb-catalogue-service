package com.needibay.cart.service.review;

import com.needibay.cart.service.FeatureProvider;
import com.netflix.discovery.provider.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    FeatureProvider featureProvider;

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
