package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardLimitRequest {
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
}