package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

	@NotNull(message = "User ID is required")
	@Positive(message = "User ID must be greater than zero")
	private Long userId;

	@NotBlank(message = "Product name is required")
	@Size(min = 2, max = 100, message = "Product name must contain between 2 and 100 characters")
	private String productName;
}