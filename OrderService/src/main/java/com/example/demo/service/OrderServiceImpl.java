package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Order;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl
        implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @Override
    public OrderResponseDto getOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        UserDto user =userClient.getUserById(order.getUserId());
        

        return OrderResponseDto.builder().orderId(order.getOrderId()).productName(order.getProductName())
                .user(user).build();
    }
    
    @Override
    @Transactional
    public OrderResponseDto createOrder( OrderCreateRequest request) {

        UserDto user =userClient.getUserById(request.getUserId());

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductName(request.getProductName());

        Order savedOrder = orderRepository.save(order);
        
    
        return OrderResponseDto.builder()
                .orderId(savedOrder.getOrderId())
                .productName(savedOrder.getProductName())
                .user(user)
                .build();
    }
    
    
    
}