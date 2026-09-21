package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.DependencyUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceResilientClient {

    private final UserClient userClient;

    @Retry(
            name = "userService",
            fallbackMethod = "getUserFallback"
    )
    @CircuitBreaker(
            name = "userService",
            fallbackMethod = "getUserFallback"
    )
    public UserDto getUserById(Long userId) {

        return userClient.getUserById(userId);
    }

    public UserDto getUserFallback(
            Long userId,
            Throwable ex) {

        throw new DependencyUnavailableException(
                "User Service is temporarily unavailable");
    }
}
