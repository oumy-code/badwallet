package com.ism.badwallet.strategies;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class StandardWithdrawalFeeStrategy implements WithdrawalFeeStrategy {
    private static final BigDecimal FEE_RATE = new BigDecimal("0.01"); 
    private static final BigDecimal MAX_FEE = new BigDecimal("5000");  

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal calculated = amount.multiply(FEE_RATE);
        
        return calculated.compareTo(MAX_FEE) > 0 ? MAX_FEE : calculated;
    }
}