package com.example.bank.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
    private Long cardId;
    private LocalDateTime createdAt;
}