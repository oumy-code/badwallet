package com.ism.badwallet.entities;

import com.ism.badwallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String walletPhone;
    private BigDecimal amount;
    private BigDecimal fees;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    private String description;
    private LocalDateTime timestamp;
}