package com.infsis.socialpagebackend.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ResponseLoggingFilter> loggingFilter() {
        FilterRegistrationBean<ResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseLoggingFilter());
        registrationBean.addUrlPatterns("/api/*"); // Add the specific URL pattern for logging
        return registrationBean;
    }
}