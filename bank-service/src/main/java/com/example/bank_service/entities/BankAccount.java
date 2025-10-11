package com.example.bank_service.entities;

import com.example.bank_service.enums.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data  @NoArgsConstructor @AllArgsConstructor @Builder

public class BankAccount {
    @Id
    private String id;
    private Date cratedAt;
    private Double balance;
    private String currency;
    private AccountType type;
}
