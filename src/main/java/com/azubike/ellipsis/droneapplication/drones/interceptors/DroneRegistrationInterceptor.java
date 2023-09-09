package com.azubike.ellipsis.droneapplication.drones.interceptors;

import com.azubike.ellipsis.droneapplication.drones.repository.DroneRepository;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class DroneRegistrationInterceptor implements HandlerInterceptor {
    private final DroneRepository droneRepository;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if ("POST".equals(request.getMethod())) {
            final long count = droneRepository.count();
            if(count >= 10) throw new ConflictException("Maximum amount of drone registration reached");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
