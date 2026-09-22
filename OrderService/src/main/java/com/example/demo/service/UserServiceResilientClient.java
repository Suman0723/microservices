package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.DependencyUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceResilientClient {

	private final UserClient userClient;

	@Bulkhead(name = "userService", fallbackMethod = "getUserFallback")

	@Retry(name = "userService", fallbackMethod = "getUserFallback")
	@CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
	public UserDto getUserById(Long userId) {
		log.info("Calling User Service for userId: {}", userId);

		return userClient.getUserById(userId);
	}

	public UserDto getUserFallback(Long userId, Throwable ex) {

		log.error("User Service fallback executed for userId: {}. Reason: {}", userId, ex.getMessage());

		throw new DependencyUnavailableException("User Service is temporarily unavailable");
	}
}
