package com.example.bank.service;


import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransferRequest;

import java.util.List;


public interface TransactionService {
    List<TransactionDto> getCardTransactions(Long cardId);
    TransactionDto transfer(TransferRequest request);
}