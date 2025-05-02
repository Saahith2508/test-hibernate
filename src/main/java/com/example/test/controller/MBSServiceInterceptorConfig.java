package com.example.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class MBSServiceInterceptorConfig implements WebMvcConfigurer
{

    @Autowired
    private CountryGroupFilterInterceptor countryGroupFilterInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(countryGroupFilterInterceptor);
    }
}