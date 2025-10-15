package com.example.bank_service.web;

import com.example.bank_service.dto.BankAccountRequestDTO;
import com.example.bank_service.dto.BankAccountResponseDTO;
import com.example.bank_service.entities.BankAccount;
import com.example.bank_service.entities.Customer;
import com.example.bank_service.repositories.BankAccountRepository;
import com.example.bank_service.repositories.CustomerRepository;
import com.example.bank_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class BankAccountGraphQlController {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountService accountService;
    @QueryMapping
    public List<BankAccount> accountsList (){
        return bankAccountRepository.findAll();
    }
    @QueryMapping
    public List<Customer> customers (){
        return customerRepository.findAll();
    }

    @QueryMapping
    public BankAccount accountById (@Argument  String id){
        return bankAccountRepository.findById(id)
                .orElseThrow(()->new RuntimeException(String.format("Account %s not found",id)));
    }
    @MutationMapping
    public BankAccountResponseDTO addAccount(@Argument BankAccountRequestDTO bankAccount){
        return accountService.addAcount(bankAccount);
    }
    @MutationMapping
    public BankAccountResponseDTO updateAccount(@Argument String id,@Argument BankAccountRequestDTO bankAccount){
        return accountService.updateAccount(id,bankAccount);
    }
    @MutationMapping
    public void deleteAccount(@Argument String id){
bankAccountRepository.deleteById(id);
    }

}
/*record BankAccountDTO(Double balance, String type,String currency){
}*/
