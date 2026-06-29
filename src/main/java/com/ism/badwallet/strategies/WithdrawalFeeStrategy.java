package com.ism.badwallet.strategies;

import java.math.BigDecimal;

public interface WithdrawalFeeStrategy {
    BigDecimal calculateFee(BigDecimal amount);
}