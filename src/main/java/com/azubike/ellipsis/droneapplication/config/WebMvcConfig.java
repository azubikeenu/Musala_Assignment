package com.azubike.ellipsis.droneapplication.config;

import com.azubike.ellipsis.droneapplication.drones.interceptors.DroneRegistrationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final  DroneRegistrationInterceptor droneRegistrationInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(droneRegistrationInterceptor)
                .addPathPatterns("/api/v1/drones") ;// Add URL patterns for which this interceptor should be applied
    }
}
