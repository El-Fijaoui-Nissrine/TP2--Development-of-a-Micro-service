package com.example.bank_service.repositories;

import com.example.bank_service.entities.BankAccount;
import com.example.bank_service.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findByType(AccountType type);
}
