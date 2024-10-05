package com.needibay.cart.component;


import com.needibay.cart.libs.ServiceProviderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

public class PostServiceCommand implements IServiceCommand {

    @Autowired
    ServiceProviderInterface serviceProviderInterface;

    public PostServiceCommand(ServiceProviderInterface theServiceProviderInterface) {
        this.serviceProviderInterface = theServiceProviderInterface;
    }

    @Override
    public Object execute() {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        // Setting Up Headers
        HttpHeaders headers = new HttpHeaders();
        headers = this.serviceProviderInterface.getHeaders();

        // URL String Formatting
        String requestUrl = String.format(this.serviceProviderInterface.getUrl());
        System.out.print("==============request url\n");
        System.out.print(requestUrl);
        System.out.print("==============request url\n");

        // Setting up request body and headers
        HttpEntity<String> httpEntity = new HttpEntity(this.serviceProviderInterface.getRequestBody(), headers);

        // API request call
         Object response = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Object.class);

        return response;

    }

    @Override
    public CompletableFuture<Object> executeAsync() {
        Object response = execute();
        return CompletableFuture.completedFuture(response);
    }
}
