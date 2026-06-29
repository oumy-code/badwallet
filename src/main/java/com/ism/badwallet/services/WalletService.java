package com.ism.badwallet.services;

import com.ism.badwallet.dtos.DepositRequest;
import com.ism.badwallet.dtos.WithdrawRequest;
import com.ism.badwallet.entities.*;
import com.ism.badwallet.enums.TransactionType;
import com.ism.badwallet.repositories.*;
import com.ism.badwallet.strategies.WithdrawalFeeStrategy;
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
    private final WithdrawalFeeStrategy feeStrategy; 

    public Wallet deposit(Long id, DepositRequest request) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
                   
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

    public Wallet withdraw(WithdrawRequest request) {
        
        Wallet wallet = walletRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
               
        
        BigDecimal fees = feeStrategy.calculateFee(request.getAmount());
        BigDecimal totalDeduction = request.getAmount().add(fees);

        
        if (wallet.getBalance().compareTo(totalDeduction) < 0) {
            throw new RuntimeException("Solde insuffisant pour le retrait et les frais");
        }

        
        wallet.setBalance(wallet.getBalance().subtract(totalDeduction));
       
        
        transactionRepository.save(TransactionHistory.builder()
                .walletPhone(wallet.getPhoneNumber())
                .amount(request.getAmount())
                .fees(fees)
                .type(TransactionType.WITHDRAW)
                .description("Retrait d'argent")
                .timestamp(LocalDateTime.now())
                .build());

        return walletRepository.save(wallet);
    }
}