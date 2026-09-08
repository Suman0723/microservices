package com.example.demo.service;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderResponseDto;

public interface OrderService {

    OrderResponseDto getOrder(Long orderId);
    OrderResponseDto createOrder(OrderCreateRequest request);

}