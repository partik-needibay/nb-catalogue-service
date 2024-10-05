package com.needibay.cart.component;

import com.needibay.cart.libs.ServiceProviderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

public class GetServiceCommand implements IServiceCommand {

    @Autowired
    ServiceProviderInterface serviceProviderInterface;

    public GetServiceCommand(ServiceProviderInterface theServiceProviderInterface) {
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

        String requestUrl = this.serviceProviderInterface.getUrl();

        // Setting up request body and headers
        HttpEntity<String> httpEntity = new HttpEntity(headers);

        Object response = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, Object.class);

        //Object response = restTemplate.getForEntity(requestUrl, Object.class);

        return response;
    }

    @Override
    public String executeString() {

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        // Setting Up Headers
        HttpHeaders headers = new HttpHeaders();
        headers = this.serviceProviderInterface.getHeaders();

        String requestUrl = this.serviceProviderInterface.getUrl();

        // Setting up request body and headers
        HttpEntity<String> httpEntity = new HttpEntity(headers);

        String response = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class).getBody();

        //Object response = restTemplate.getForEntity(requestUrl, Object.class);

        return response;
    }


    @Override
    public CompletableFuture<Object> executeAsync() {
        Object response = execute();
        return CompletableFuture.completedFuture(response);
    }
}
