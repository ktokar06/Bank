package com.example.bank.service;


import com.example.bank.dto.LoginRequest;
import com.example.bank.dto.RegisterRequest;


public interface AuthService {
    String login(LoginRequest request);
    String register(RegisterRequest request);
}