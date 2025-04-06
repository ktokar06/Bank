package com.example.bank.service;

import com.bank.dto.LoginRequest;
import com.bank.dto.RegisterRequest;

public interface AuthService {
    String login(LoginRequest request);
    String register(RegisterRequest request);
}