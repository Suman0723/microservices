package com.example.demo.service;

import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponseDto;

public interface UserService {

    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserCreateRequest request);

}