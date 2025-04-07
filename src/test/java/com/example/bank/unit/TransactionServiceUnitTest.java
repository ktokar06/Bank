package com.example.bank.unit;

import com.example.bank.dto.TransferRequest;
import com.example.bank.exception.LimitExceededException;
import com.example.bank.model.Card;
import com.example.bank.repository.CardRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceUnitTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void transferExceedsDailyLimit() {
        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setDailyLimit(new BigDecimal("100"));
        fromCard.setBalance(new BigDecimal("500"));
        fromCard.setStatus(Card.CardStatus.ACTIVE);

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setStatus(Card.CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(transactionRepository.sumAmountByCardAndDate(anyLong(), any(), any()))
                .thenReturn(Optional.of(new BigDecimal("900")));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(new BigDecimal("200"));

        assertThrows(LimitExceededException.class,
                () -> transactionService.transfer(request));
    }
}