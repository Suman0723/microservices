package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@GetMapping("/{orderId}")
	public PaymentDto getPaymentByOrderId(@PathVariable Long orderId) throws InterruptedException {

		Thread.sleep(1000);

		return PaymentDto.builder().paymentId(1001L).paymentStatus("SUCCESS").amount(1500.00).build();
	}
}