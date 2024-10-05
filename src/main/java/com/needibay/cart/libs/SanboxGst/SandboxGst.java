package com.needibay.cart.libs.SanboxGst;

import com.needibay.cart.libs.ServiceProviderInterface;
import com.needibay.cart.service.FeatureProvider;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@Data
public class SandboxGst implements ServiceProviderInterface {

    @Autowired
    FeatureProvider featureProvider;

    private String sandboxGstUrl;

    private Object sandboxGstRequestBody;

    private HttpHeaders sandboxGstHeaders;

    @Override
    public Object getRequestBody() {
        return this.getSandboxGstRequestBody();
    }


    @Override
    public HttpHeaders getHeaders() {
        return this.getSandboxGstHeaders();
    }

    @Override
    public String getUrl() {
        return this.getSandboxGstUrl();
    }


    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
