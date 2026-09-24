package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Order;
import com.example.demo.exception.DependencyUnavailableException;
import com.example.demo.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserServiceResilientClient userServiceResilientClient;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrderShouldReturnSuccessfulResponse() {

        OrderCreateRequest request =
                new OrderCreateRequest(1L, "Laptop");

        UserDto user = new UserDto(
                1L,
                "Suman",
                "suman@example.com"
        );

        Order savedOrder = Order.builder()
                .orderId(10L)
                .userId(1L)
                .productName("Laptop")
                .build();

        PaymentDto payment = PaymentDto.builder()
                .paymentId(1001L)
                .paymentStatus("SUCCESS")
                .amount(1500.0)
                .build();

        when(userServiceResilientClient.getUserById(1L))
                .thenReturn(user);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        when(paymentClient.getPaymentByOrderId(10L))
                .thenReturn(payment);

        OrderResponseDto response =
                orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(10L, response.getOrderId());
        assertEquals("Laptop", response.getProductName());
        assertEquals(1L, response.getUser().getId());
        assertEquals(
                "SUCCESS",
                response.getPayment().getPaymentStatus()
        );
        assertEquals(
                1500.0,
                response.getPayment().getAmount()
        );

        verify(userServiceResilientClient)
                .getUserById(1L);

        verify(orderRepository)
                .save(any(Order.class));

        verify(paymentClient)
                .getPaymentByOrderId(10L);
    }

    @Test
    void createOrderShouldNotSaveWhenUserServiceIsUnavailable() {

        OrderCreateRequest request =
                new OrderCreateRequest(1L, "Laptop");

        when(userServiceResilientClient.getUserById(1L))
                .thenThrow(
                        new DependencyUnavailableException(
                                "User Service is temporarily unavailable"
                        )
                );

        DependencyUnavailableException exception =
                assertThrows(
                        DependencyUnavailableException.class,
                        () -> orderService.createOrder(request)
                );

        assertEquals(
                "User Service is temporarily unavailable",
                exception.getMessage()
        );

        verify(userServiceResilientClient)
                .getUserById(1L);

        verify(orderRepository, never())
                .save(any(Order.class));

        verify(paymentClient, never())
                .getPaymentByOrderId(any());
    }

    @Test
    void createOrderFallbackShouldThrowExceptionForPaymentFailure() {

        OrderCreateRequest request =
                new OrderCreateRequest(1L, "Laptop");

        RuntimeException paymentFailure =
                new RuntimeException(
                        "Payment Service connection failed"
                );

        DependencyUnavailableException exception =
                assertThrows(
                        DependencyUnavailableException.class,
                        () -> orderService.createOrderFallback(
                                request,
                                paymentFailure
                        )
                );

        assertEquals(
                "Payment Service is temporarily unavailable",
                exception.getMessage()
        );
    }
}