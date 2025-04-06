package com.example.bank.dto;


import com.example.bank.model.Card;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class CardDto {
    private Long id;
    private String number;
    private LocalDate expirationDate;
    private Card.CardStatus status;
    private BigDecimal balance;
    private String ownerName;
    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;
    private Long userId;
    private String cvv;
}