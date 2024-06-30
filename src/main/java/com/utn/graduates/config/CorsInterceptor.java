package com.utn.graduates.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CorsInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CorsInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isCorsError(response)) {
            logger.error("Error in CORS when tried to proccess the request: METHOD {}, URL {}", request.getMethod(), request.getRequestURI());
        }
        return true;
    }

    private boolean isCorsError(HttpServletResponse response) {
        return response.getStatus() == HttpServletResponse.SC_FORBIDDEN
                || response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}