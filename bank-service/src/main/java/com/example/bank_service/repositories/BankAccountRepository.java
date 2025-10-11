package com.example.bank_service.repositories;

import com.example.bank_service.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
