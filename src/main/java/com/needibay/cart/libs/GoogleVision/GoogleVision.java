package com.needibay.cart.libs.GoogleVision;

import com.needibay.cart.libs.ServiceProviderInterface;
import com.needibay.cart.service.FeatureProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@Data
public class GoogleVision implements ServiceProviderInterface {

    @Autowired
    FeatureProvider featureProvider;

    private String googleVisionAPIUrl;

    private Object googleVisionAPIRequestBody;

    private HttpHeaders googleVisionAPIHeaders;

    @Override
    public Object getRequestBody() {
        return this.getGoogleVisionAPIRequestBody();
    }


    @Override
    public HttpHeaders getHeaders() {
        return this.getGoogleVisionAPIHeaders();
    }

    @Override
    public String getUrl() {
        return this.getGoogleVisionAPIUrl();
    }


    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
