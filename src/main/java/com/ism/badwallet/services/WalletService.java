package com.ism.badwallet.services;

import com.ism.badwallet.dtos.WalletCreationRequest;
import com.ism.badwallet.dtos.DepositRequest;
import com.ism.badwallet.dtos.PaymentRequest;
import com.ism.badwallet.dtos.TransferRequest;
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
    @org.springframework.scheduling.annotation.Async
    public void seedDatabase(int numWallets, int eventsPerWallet) {
        java.util.Random random = new java.util.Random();
        
        for (int i = 1; i <= numWallets; i++) {
            String phone = "+221770000" + String.format("%03d", i);
            
            
            Wallet wallet = Wallet.builder()
                    .phoneNumber(phone)
                    .email("user" + i + "@ism.edu.sn")
                    .balance(new BigDecimal("100000")) 
                    .code("WLT-" + String.format("%07d", i))
                    .currency("XOF")
                    .build();
            walletRepository.save(wallet);

       
            for (int j = 1; j <= eventsPerWallet; j++) {
                TransactionHistory tx = TransactionHistory.builder()
                        .walletPhone(phone)
                        .amount(new BigDecimal(random.nextInt(5000) + 500))
                        .fees(BigDecimal.ZERO)
                        .type(TransactionType.DEPOSIT)
                        .description("Seed transaction n°" + j)
                        .timestamp(LocalDateTime.now())
                        .build();
                transactionRepository.save(tx);
            }
        }
    }
    public Wallet createWallet(WalletCreationRequest request) {
       
        Wallet wallet = Wallet.builder()
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .balance(request.getInitialBalance())
                .code(request.getCode())
                .currency(request.getCurrency())
                .build();
                
        return walletRepository.save(wallet);
    }
    public org.springframework.data.domain.Page<Wallet> getAllWallets(org.springframework.data.domain.Pageable pageable) {
        return walletRepository.findAll(pageable);
    }
    public Wallet getWalletByPhone(String phone) {
        return walletRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
    }

    public java.math.BigDecimal getBalance(String phone) {
        return this.getWalletByPhone(phone).getBalance();
    }
    public void transfer(TransferRequest request) {
        // 1. Récupérer le portefeuille de l'émetteur
        Wallet sender = walletRepository.findByPhoneNumber(request.getSenderPhone())
                .orElseThrow(() -> new RuntimeException("Émetteur non trouvé"));
                
       
        Wallet receiver = walletRepository.findByPhoneNumber(request.getReceiverPhone())
                .orElseThrow(() -> new RuntimeException("Récepteur non trouvé"));

       
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Solde insuffisant pour effectuer le transfert");
        }

       
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        walletRepository.save(sender);
        walletRepository.save(receiver);

       
        transactionRepository.save(TransactionHistory.builder()
                .walletPhone(sender.getPhoneNumber())
                .amount(request.getAmount())
                .fees(BigDecimal.ZERO)
                .type(TransactionType.TRANSFER)
                .description("Transfert envoyé à " + receiver.getPhoneNumber())
                .timestamp(LocalDateTime.now())
                .build());
    }
    public void payFacture(PaymentRequest request) {
      
        Wallet wallet = walletRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));

       
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Solde insuffisant pour régler cette facture");
        }

       
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

       
        String desc = "Paiement service " + request.getServiceName();
        if (request.getFactureReferences() != null && !request.getFactureReferences().isEmpty()) {
            desc += " (Factures: " + String.join(", ", request.getFactureReferences()) + ")";
        }

       
        transactionRepository.save(TransactionHistory.builder()
                .walletPhone(wallet.getPhoneNumber())
                .amount(request.getAmount())
                .fees(BigDecimal.ZERO)
                .type(TransactionType.PAYMENT)
                .description(desc)
                .timestamp(LocalDateTime.now())
                .build());
    }
}