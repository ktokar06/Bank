package com.example.bank.service;


import com.example.bank.dto.TransactionDto;
import com.example.bank.dto.TransferRequest;
import com.example.bank.exception.*;
import com.example.bank.model.*;
import com.example.bank.model.Transaction;
import com.example.bank.repository.CardRepository;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getCardTransactions(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException("Card not found with id: " + cardId);
        }

        return transactionRepository.findByCardId(cardId).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionDto transfer(TransferRequest request) {
        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("Source card not found"));
        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("Destination card not found"));

        validateTransfer(fromCard, toCard, request.getAmount());

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        Transaction withdrawal = createTransaction(
                request.getAmount().negate(),
                "TRANSFER_OUT",
                request.getDescription(),
                fromCard
        );

        Transaction deposit = createTransaction(
                request.getAmount(),
                "TRANSFER_IN",
                request.getDescription(),
                toCard
        );

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        transactionRepository.save(withdrawal);
        transactionRepository.save(deposit);

        return modelMapper.map(withdrawal, TransactionDto.class);
    }

    private void validateTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        if (fromCard.getStatus() != Card.CardStatus.ACTIVE) {
            throw new CardOperationException("Source card is not active");
        }
        if (toCard.getStatus() != Card.CardStatus.ACTIVE) {
            throw new CardOperationException("Destination card is not active");
        }
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on source card");
        }

        BigDecimal dailySpent = transactionRepository.sumAmountByCardAndDate(
                fromCard.getId(),
                LocalDateTime.now().withHour(0).withMinute(0),
                LocalDateTime.now()
        ).orElse(BigDecimal.ZERO);

        if (dailySpent.add(amount).compareTo(fromCard.getDailyLimit()) > 0) {
            throw new LimitExceededException("Daily limit exceeded");
        }
    }

    private Transaction createTransaction(BigDecimal amount, String type, String description, Card card) {
        return Transaction.builder()
                .amount(amount)
                .type(type)
                .description(description)
                .card(card)
                .createdAt(LocalDateTime.now())
                .build();
    }
}