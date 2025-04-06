package com.example.bank.service;

import com.bank.dto.RegisterRequest;
import com.bank.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto register(RegisterRequest request);
    List<UserDto> getAllUsers();
    UserDto getUser(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}