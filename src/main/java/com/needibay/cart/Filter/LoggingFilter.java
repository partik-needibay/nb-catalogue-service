package com.needibay.cart.Filter;

import com.needibay.cart.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;


@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Autowired
    private EmailUtil emailUtil;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            logger.info("Incoming request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

            // Continue with the filter chain
            chain.doFilter(request, response);

            logger.info("Outgoing response: {}", httpResponse.getStatus());

        } catch (Exception ex) {
            logger.error("An error occurred during request processing", ex);
            emailUtil.sendErrorEmail("Error in GlobalExceptionHandler", ex);
            throw ex;
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
