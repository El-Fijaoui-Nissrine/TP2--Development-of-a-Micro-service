package com.example.bank_service.service;

import com.example.bank_service.dto.BankAccountRequestDTO;
import com.example.bank_service.dto.BankAccountResponseDTO;

public interface AccountService {
    public BankAccountResponseDTO addAcount(BankAccountRequestDTO bankAccountRequestDTO);
}
