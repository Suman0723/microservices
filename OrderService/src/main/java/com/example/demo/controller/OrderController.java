package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.getOrder(id));
    }
    
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
    @RequestBody OrderCreateRequest request) {
    
    return ResponseEntity.status(HttpStatus.CREATED)
    .body(orderService.createOrder(request));
    }
}