package com.needibay.cart.libs;


import com.needibay.cart.service.FeatureProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@Data
public class PostRequest implements ServiceProviderInterface{

    @Autowired
    FeatureProvider featureProvider;

    private String reqUrl;

    private Object reqBody;

    private HttpHeaders reqHeaders;

    @Override
    public Object getRequestBody() {
        return this.getReqBody();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.getReqHeaders();
    }

    @Override
    public String getUrl() {
        return this.getReqUrl();
    }

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
