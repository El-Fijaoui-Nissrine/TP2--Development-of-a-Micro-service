package com.example.bank_service;

import com.example.bank_service.entities.BankAccount;
import com.example.bank_service.enums.AccountType;
import com.example.bank_service.repositories.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class BankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}
@Bean
	CommandLineRunner start(BankAccountRepository bankAccountRepository){
		return atgs->{
for (int i =1 ;i<10; i++){
	BankAccount bankAccount=BankAccount.builder()
			.id(UUID.randomUUID().toString())
			.cratedAt(new Date())
			.balance(10000+Math.random()*90000)
			.type(Math.random()>0.5? AccountType.CURRENT_ACCOUNT:AccountType.SAVING_ACCOUNT)
			.currency("MAD")
			.build();
	bankAccountRepository.save(bankAccount);


}


		};


	}

}
