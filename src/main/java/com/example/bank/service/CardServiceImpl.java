package com.example.bank.service;

import com.bank.dto.CardDto;
import com.bank.dto.CardLimitRequest;
import com.bank.exception.*;
import com.bank.model.Card;
import com.bank.model.User;
import com.bank.repository.CardRepository;
import com.bank.repository.UserRepository;
import com.bank.security.CardDataEncryptor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CardDto createCard(CardDto cardDto) {
        User user = userRepository.findById(cardDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + cardDto.getUserId()));

        Card card = modelMapper.map(cardDto, Card.class);
        card.setNumber(CardDataEncryptor.encrypt(cardDto.getNumber()));
        card.setCvv(CardDataEncryptor.encrypt(cardDto.getCvv()));
        card.setUser(user);
        card.setBalance(BigDecimal.ZERO);
        card.setDailyLimit(new BigDecimal("1000.00"));
        card.setMonthlyLimit(new BigDecimal("5000.00"));

        Card savedCard = cardRepository.save(card);
        return modelMapper.map(savedCard, CardDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto getCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        // Маскируем номер карты перед возвратом
        CardDto dto = modelMapper.map(card, CardDto.class);
        dto.setNumber(CardDataEncryptor.maskCardNumber(card.getNumber()));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> getUserCards(Long userId) {
        return cardRepository.findByUserId(userId).stream()
                .map(card -> {
                    CardDto dto = modelMapper.map(card, CardDto.class);
                    dto.setNumber(CardDataEncryptor.maskCardNumber(card.getNumber()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDto blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (card.getStatus() == Card.CardStatus.BLOCKED) {
            throw new CardOperationException("Card is already blocked");
        }

        card.setStatus(Card.CardStatus.BLOCKED);
        Card savedCard = cardRepository.save(card);
        return modelMapper.map(savedCard, CardDto.class);
    }

    @Override
    @Transactional
    public CardDto activateCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (card.getStatus() == Card.CardStatus.ACTIVE) {
            throw new CardOperationException("Card is already active");
        }

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new CardOperationException("Cannot activate expired card");
        }

        card.setStatus(Card.CardStatus.ACTIVE);
        Card savedCard = cardRepository.save(card);
        return modelMapper.map(savedCard, CardDto.class);
    }

    @Override
    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (card.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new CardOperationException("Cannot delete card with positive balance");
        }

        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public CardDto updateLimits(Long cardId, CardLimitRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

        if (request.getDailyLimit().compareTo(BigDecimal.ZERO) < 0 ||
                request.getMonthlyLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidLimitException("Limits cannot be negative");
        }

        card.setDailyLimit(request.getDailyLimit());
        card.setMonthlyLimit(request.getMonthlyLimit());

        Card updatedCard = cardRepository.save(card);
        return modelMapper.map(updatedCard, CardDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(card -> {
                    CardDto dto = modelMapper.map(card, CardDto.class);
                    dto.setNumber(CardDataEncryptor.maskCardNumber(card.getNumber()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}