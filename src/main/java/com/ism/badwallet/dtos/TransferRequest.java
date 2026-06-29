package com.ism.badwallet.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String senderPhone;   
    private String receiverPhone; 
    private BigDecimal amount;    
}