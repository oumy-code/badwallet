package com.ism.badwallet.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositRequest {
    private BigDecimal amount;
    private String paymentMethod; 
}