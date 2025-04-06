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

    public CardDto(Long id, String number, LocalDate expirationDate, Card.CardStatus status, BigDecimal balance, String ownerName, BigDecimal dailyLimit, BigDecimal monthlyLimit, Long userId, String cvv) {
        this.id = id;
        this.number = number;
        this.expirationDate = expirationDate;
        this.status = status;
        this.balance = balance;
        this.ownerName = ownerName;
        this.dailyLimit = dailyLimit;
        this.monthlyLimit = monthlyLimit;
        this.userId = userId;
        this.cvv = cvv;
    }

    public CardDto(String number, String date, String number1, String testUser, double v, String active, double v1, double v2, long userId) {
    }
}