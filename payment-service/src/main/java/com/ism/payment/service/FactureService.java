package com.ism.payment.service;

import com.ism.payment.entity.Facture;
import com.ism.payment.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureService implements CommandLineRunner {

    private final FactureRepository factureRepository;

    // Seed automatique au démarrage
    @Override
    public void run(String... args) {
        List<Facture> factures = new ArrayList<>();
        String[] unites = {"ISM", "WOYAFAL"};
        LocalDate now = LocalDate.now();

        for (int w = 1; w <= 10; w++) {
            String walletCode = String.format("WLT-%07d", w);
            for (String unite : unites) {
                for (int i = 1; i <= 3; i++) {
                    factures.add(Facture.builder()
                            .reference("FAC-" + unite + "-" + w + "-" + i)
                            .walletCode(walletCode)
                            .unite(unite)
                            .montant(new BigDecimal(5000 * i))
                            .dateFacture(now.minusMonths(i - 1))
                            .payee(false)
                            .build());
                }
            }
        }
        factureRepository.saveAll(factures);
        System.out.println("✅ Payment-service: " + factures.size() + " factures seedées.");
    }

    public List<Facture> getFacturesCurrent(String walletCode, String unite) {
        LocalDate debut = LocalDate.now().withDayOfMonth(1);
        LocalDate fin = debut.plusMonths(1).minusDays(1);

        if (unite != null && !unite.isBlank()) {
            return factureRepository.findByWalletCodeAndPayeeFalseAndUniteAndDateFactureBetween(
                    walletCode, unite, debut, fin);
        }
        return factureRepository.findByWalletCodeAndPayeeFalseAndDateFactureBetween(
                walletCode, debut, fin);
    }

    public List<Facture> getFacturesByPeriode(String walletCode, LocalDate debut, LocalDate fin) {
        return factureRepository.findByWalletCodeAndPayeeFalseAndDateFactureBetween(
                walletCode, debut, fin);
    }

    public void payerFactures(List<String> references) {
        List<Facture> factures = factureRepository.findByReferenceIn(references);
        factures.forEach(f -> f.setPayee(true));
        factureRepository.saveAll(factures);
    }
}
