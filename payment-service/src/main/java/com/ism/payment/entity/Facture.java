package com.ism.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;       // ex: FAC-ISM-3-1
    private String walletCode;      // ex: WLT-0000003
    private String unite;           // ex: ISM, WOYAFAL
    private BigDecimal montant;
    private LocalDate dateFacture;
    private boolean payee;
}
