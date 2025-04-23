package com.infsis.socialpagebackend.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Wrap response to capture the response body
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);

        // Log response status and body
        logger.info("Response Status: {} | Response Body: {}", responseWrapper.getStatus(), new String(responseWrapper.getResponseData(), StandardCharsets.UTF_8));
    }
}
