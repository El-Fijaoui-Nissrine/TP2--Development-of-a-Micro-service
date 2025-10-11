package com.example.bank_service.dto;

import com.example.bank_service.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BankAccountResponseDTO {

    private String id;
    private Date cratedAt;
    private Double balance;
    private String currency;
    private AccountType type;
}
