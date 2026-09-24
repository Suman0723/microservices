package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;
import java.util.LinkedHashMap;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FeignException.NotFound.class)
	public ResponseEntity<Map<String, String>> handleUserNotFound() {

		Map<String, String> error = new HashMap<>();

		error.put("message", "User not found");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<Map<String, String>> handleFeignException() {

		Map<String, String> error = new HashMap<>();

		error.put("message", "User Service unavailable");

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleOrderNotFound(ResourceNotFoundException ex) {

		Map<String, String> error = new HashMap<>();
		error.put("message", ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(DependencyUnavailableException.class)
	public ResponseEntity<Map<String, String>> handleDependencyUnavailable(DependencyUnavailableException ex) {

		Map<String, String> error = new HashMap<>();
		error.put("message", ex.getMessage());

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

		Map<String, String> fieldErrors = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "VALIDATION_FAILED");
		response.put("message", "Request validation failed");
		response.put("validationErrors", fieldErrors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}