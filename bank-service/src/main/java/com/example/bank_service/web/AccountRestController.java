package com.example.bank_service.web;

import com.example.bank_service.dto.BankAccountRequestDTO;
import com.example.bank_service.dto.BankAccountResponseDTO;
import com.example.bank_service.entities.BankAccount;
import com.example.bank_service.repositories.BankAccountRepository;
import com.example.bank_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AccountRestController {
    @Autowired
  private BankAccountRepository bankAccountRepository;
    @Autowired
    private AccountService accountService;
@GetMapping("/bankAccounts")
    public List<BankAccount>bankAccounts(){
    return  bankAccountRepository.findAll();
}
    @GetMapping("/bankAccounts/{id}")
    public BankAccount bankAccount(@PathVariable  String  id){
        return  bankAccountRepository.findById(id).orElseThrow(()->new RuntimeException(String.format("Account %s nit found",id)));
    }
    @PostMapping("/bankAccounts")
public BankAccountResponseDTO save(@RequestBody BankAccountRequestDTO requestDTO){
    return accountService.addAcount(requestDTO);
    }
    @PutMapping("/bankAccounts/{id}")
    public BankAccount update(@PathVariable String id, @RequestBody BankAccount bankAccount){
    BankAccount account=bankAccountRepository.findById(id).orElseThrow();
   if (bankAccount.getBalance()!=null)   account.setBalance(bankAccount.getBalance());
        if (bankAccount.getType()!=null) account.setType(bankAccount.getType());
        if (bankAccount.getCurrency()!=null) account.setCurrency(bankAccount.getCurrency());
        if (bankAccount.getCratedAt()!=null) account.setCratedAt(bankAccount.getCratedAt());
    return bankAccountRepository.save(account);
    }
    @DeleteMapping("/bankAccounts/{id}")
    public void deleteAccount(@PathVariable  String  id){
       bankAccountRepository.deleteById(id);
    }
}
