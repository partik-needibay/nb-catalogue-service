package com.needibay.cart.service.invoice;

import com.needibay.cart.service.FeatureProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    FeatureProvider featureProvider;

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
