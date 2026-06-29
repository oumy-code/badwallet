package com.ism.payment.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentRequest {
    private String walletCode;
    private String serviceName;
    private BigDecimal amount;
    private List<String> factureReferences;
}
