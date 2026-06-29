package com.ism.payment.controller;

import com.ism.payment.dto.PaymentRequest;
import com.ism.payment.entity.Facture;
import com.ism.payment.service.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/external/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    // 2.2 & 2.3 : Factures du mois en cours (avec ou sans filtre unite)
    @GetMapping("/{walletCode}/current")
    public ResponseEntity<List<Facture>> getCurrent(
            @PathVariable String walletCode,
            @RequestParam(required = false) String unite) {
        return ResponseEntity.ok(factureService.getFacturesCurrent(walletCode, unite));
    }

    // 2.4 : Factures sur une période
    @GetMapping("/{walletCode}/periode")
    public ResponseEntity<List<Facture>> getByPeriode(
            @PathVariable String walletCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(factureService.getFacturesByPeriode(walletCode, debut, fin));
    }

    // Endpoint appelé par badwallet-api pour payer des factures
    @PostMapping("/pay")
    public ResponseEntity<Void> pay(@RequestBody PaymentRequest request) {
        if (request.getFactureReferences() != null) {
            factureService.payerFactures(request.getFactureReferences());
        }
        return ResponseEntity.ok().build();
    }
}
