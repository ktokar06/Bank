package com.example.bank.unit;

import com.example.bank.dto.CardDto;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.model.User;
import com.example.bank.repository.CardRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.service.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceUnitTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard_WithValidUser_ShouldReturnMaskedCard() {
        User user = new User();
        user.setId(1L);
        CardDto request = new CardDto();
        request.setUserId(1L);
        request.setNumber("4111111111111111");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CardDto result = cardService.createCard(request);

        assertNotNull(result);
        assertTrue(result.getNumber().startsWith("4111******"));
    }

    @Test
    void createCard_WithInvalidUser_ShouldThrowException() {

        CardDto request = new CardDto();
        request.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardService.createCard(request));
    }
}