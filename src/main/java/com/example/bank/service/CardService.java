package com.example.bank.service;

import com.bank.dto.CardDto;
import com.bank.dto.CardLimitRequest;

import java.util.List;

public interface CardService {
    CardDto createCard(CardDto cardDto);
    CardDto getCard(Long id);
    List<CardDto> getAllCards();
    List<CardDto> getUserCards(Long userId);
    CardDto blockCard(Long id);
    CardDto activateCard(Long id);
    void deleteCard(Long id);
    CardDto updateLimits(Long cardId, CardLimitRequest request);
}