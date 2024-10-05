package com.needibay.cart.service;

public interface Feature {

    static FeatureProvider featureProviderFactory(){
        return new FeatureProvider();
    }

}