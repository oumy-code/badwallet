package com.ism.payment.repository;

import com.ism.payment.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findByWalletCodeAndPayeeFalseAndDateFactureBetween(
            String walletCode, LocalDate debut, LocalDate fin);

    List<Facture> findByWalletCodeAndPayeeFalseAndUniteAndDateFactureBetween(
            String walletCode, String unite, LocalDate debut, LocalDate fin);

    List<Facture> findByReferenceIn(List<String> references);
}
