package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.PaymentDto;

@FeignClient( name = "PAYMENTSERVICE")
public interface PaymentClient {

    @GetMapping("/payment/{orderId}")
    PaymentDto getPaymentByOrderId(@PathVariable Long orderId);
}