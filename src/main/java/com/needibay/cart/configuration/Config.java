package com.needibay.cart.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

@Configuration(proxyBeanMethods = false)
public class Config {

    @Bean
    public ModelMap model(){
        return new ModelMap();
    }

}