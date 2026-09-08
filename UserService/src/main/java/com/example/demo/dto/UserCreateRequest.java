package com.example.demo.dto;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String name;
    private String email;
}