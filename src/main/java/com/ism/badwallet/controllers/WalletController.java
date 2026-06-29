package com.ism.badwallet.controllers;

import com.ism.badwallet.dtos.DepositRequest;
import com.ism.badwallet.dtos.WithdrawRequest;
import com.ism.badwallet.entities.TransactionHistory;
import com.ism.badwallet.entities.Wallet;
import com.ism.badwallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable Long id, @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(id, request));
    }

   
    @PostMapping("/withdraw")
    public ResponseEntity<Wallet> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request));
    }
    
    @PostMapping("/seed")
    public ResponseEntity<String> seed(@RequestParam int numWallets, @RequestParam int eventsPerWallet) {
        walletService.seedDatabase(numWallets, eventsPerWallet);
        return ResponseEntity.accepted().body("Le seeding de la base de données a démarré de manière asynchrone.");
    }
    
    @PostMapping
    public ResponseEntity<Wallet> create(@RequestBody com.ism.badwallet.dtos.WalletCreationRequest request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }
    
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Wallet>> list(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(walletService.getAllWallets(org.springframework.data.domain.PageRequest.of(page, size)));
    }
    
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Wallet> getByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(walletService.getWalletByPhone(phoneNumber));
    }

    
    @GetMapping("/{phoneNumber}/balance")
    public ResponseEntity<java.math.BigDecimal> getBalance(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(walletService.getBalance(phoneNumber));
    }
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody com.ism.badwallet.dtos.TransferRequest request) {
        walletService.transfer(request);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/pay")
    public ResponseEntity<Void> pay(@RequestBody com.ism.badwallet.dtos.PaymentRequest request) {
        walletService.payFacture(request);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/pay-factures")
    public ResponseEntity<Void> payFactures(@RequestBody com.ism.badwallet.dtos.PaymentRequest request) {
        walletService.payFacture(request);
        return ResponseEntity.ok().build();
    }

    
    @GetMapping("/{phoneNumber}/transactions")
    public ResponseEntity<org.springframework.data.domain.Page<TransactionHistory>> getTransactions(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(walletService.getTransactions(phoneNumber, org.springframework.data.domain.PageRequest.of(page, size)));
    }
}