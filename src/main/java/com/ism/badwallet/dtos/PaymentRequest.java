package com.ism.badwallet.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentRequest {
    private String phoneNumber;
    private String serviceName; 
    private BigDecimal amount;
    private List<String> factureReferences; 
}