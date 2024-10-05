package com.needibay.cart.libs;

import com.needibay.cart.service.FeatureProvider;
import org.springframework.http.HttpHeaders;


public interface ServiceProviderInterface {

    public Object getRequestBody();

    public HttpHeaders getHeaders();

    public String getUrl();

    public FeatureProvider getFeature();
}
