package com.example.bank.controller;

import com.example.bank.dto.CardDto;
import com.example.bank.dto.CardLimitRequest;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.model.User;
import com.example.bank.repository.UserRepository;
import com.example.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CardDto> getCard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.ok(cardService.createCard(cardDto));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CardDto>> getUserCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(cardService.getUserCards(user.getId()));
    }

    @PutMapping("/{id}/limits")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardDto> updateLimits(
            @PathVariable("id") Long id,
            @RequestBody CardLimitRequest limitRequest) {
        return ResponseEntity.ok(cardService.updateLimits(id, limitRequest));
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardDto> blockCard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.blockCard(id));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardDto> activateCard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.activateCard(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(@PathVariable("id") Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}