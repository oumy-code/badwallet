package com.ism.badwallet.services;

import com.ism.badwallet.dtos.DepositRequest;
import com.ism.badwallet.entities.*;
import com.ism.badwallet.enums.TransactionType;
import com.ism.badwallet.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public Wallet deposit(Long id, DepositRequest request) {
        // 1. Chercher le portefeuille par son ID, sinon lever une erreur
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
                
        // 2. Augmenter le solde actuel avec le montant du dépôt
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        
        
        transactionRepository.save(TransactionHistory.builder()
                .walletPhone(wallet.getPhoneNumber())
                .amount(request.getAmount())
                .fees(BigDecimal.ZERO)
                .type(TransactionType.DEPOSIT)
                .description("Dépôt effectué via " + request.getPaymentMethod())
                .timestamp(LocalDateTime.now())
                .build());
                
        return walletRepository.save(wallet);
    }
}