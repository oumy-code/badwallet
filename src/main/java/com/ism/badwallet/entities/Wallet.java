package com.ism.badwallet.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private BigDecimal balance;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    private String currency; // XOF, etc.
}