package com.example.bank.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String type;
    private String description;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}