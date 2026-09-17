package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.PaymentDto;
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
    private final PaymentClient paymentClient;

    @Override
    @CircuitBreaker(name = "paymentService",fallbackMethod = "getOrderFallback")
    public OrderResponseDto getOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        UserDto user =userClient.getUserById(order.getUserId());
        
        PaymentDto payment =paymentClient.getPaymentByOrderId(orderId);

        return OrderResponseDto.builder().orderId(order.getOrderId()).productName(order.getProductName())
                .user(user).payment(payment).build();
    }
    
    @Override
    @Transactional
    @CircuitBreaker(name = "paymentService",fallbackMethod = "createOrderFallback")
    public OrderResponseDto createOrder( OrderCreateRequest request) {

        UserDto user =userClient.getUserById(request.getUserId());

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductName(request.getProductName());

        Order savedOrder = orderRepository.save(order);
        
        PaymentDto payment =paymentClient.getPaymentByOrderId(savedOrder.getOrderId());

        return OrderResponseDto.builder()
                .orderId(savedOrder.getOrderId())
                .productName(savedOrder.getProductName())
                .user(user)
                .payment(payment)
                .build();
    }
    
    public OrderResponseDto getOrderFallback(Long orderId,Exception ex) 
    {return OrderResponseDto.builder()
    		.orderId(orderId)
    		.productName("Unavailable")
    		.payment(
    		PaymentDto.builder()
    		.paymentStatus(
    		"Payment Service Unavailable")
    		.build())
    		.build();
    		}
    
    public OrderResponseDto createOrderFallback(OrderCreateRequest request,Exception ex) {
    	
    		UserDto user =userClient.getUserById(request.getUserId());
    		return OrderResponseDto.builder().productName(request.getProductName()).user(user)
    		.payment(PaymentDto.builder().paymentStatus("Payment Service Temporarily Unavailable").build())
    		.build();
    		}
    
}