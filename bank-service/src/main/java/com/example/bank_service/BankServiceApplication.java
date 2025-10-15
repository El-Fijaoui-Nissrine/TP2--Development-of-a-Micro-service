package com.example.bank_service;

import com.example.bank_service.entities.BankAccount;
import com.example.bank_service.entities.Customer;
import com.example.bank_service.enums.AccountType;
import com.example.bank_service.repositories.BankAccountRepository;
import com.example.bank_service.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}
@Bean
	CommandLineRunner start(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository){
		return atgs->{
			Stream.of("yassine","nissrine","imane").forEach(c->{
				Customer customer=Customer.builder()
								.name(c)
										.build();
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer -> {
						for (int i =1 ;i<10; i++){
							BankAccount bankAccount=BankAccount.builder()
									.id(UUID.randomUUID().toString())
									.cratedAt(new Date())
									.balance(10000+Math.random()*90000)
									.type(Math.random()>0.5? AccountType.CURRENT_ACCOUNT:AccountType.SAVING_ACCOUNT)
									.currency("MAD")
									.customer(customer)
									.build();
							bankAccountRepository.save(bankAccount);


						}
					}
					);



		};


	}

}
