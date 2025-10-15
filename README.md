# Bank Account Microservice
A Spring Boot microservice for managing bank accounts with REST and GraphQL APIs.
## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Project Architecture](#project-architecture)
- [Testing](#testing)
## Overview
This microservice provides a complete banking account management system with:
- REST API for traditional CRUD operations
- GraphQL API for flexible data querying
- H2 In-Memory Database for development and testing
- Spring Data JPA for data persistence
- DTOs and Mappers for clean architecture
##  Technologies Used
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **H2 Database**
- **GraphQL Spring Boot**
-  **Spring Web**
- **Lombok**
- **Maven**
##  Project Architecture
### Project Structure
![Project Structure](./images/strctPrjGP.png)
### 1. Entities

**BankAccount Entity:**
Represents a bank account entity stored in the database.
Contains fields like balance, currency, and type, and is linked to a Customer.
```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id
    private String id;
    private Date cratedAt;
    private Double balance;
    private String currency;
    private AccountType type;
    @ManyToOne
    private Customer customer;
}
```

**Customer Entity:**
Represents a customer who owns one or more bank accounts.
Defines a one-to-many relationship with BankAccount.
```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
```

#### 2. Enums
Defines possible types of bank accounts (Current or Saving).
```java
public enum AccountType {
    CURRENT_ACCOUNT, SAVING_ACCOUNT
}
```

#### 3. DTOs

**Request DTO:**
```java
public class BankAccountRequestDTO {
    private Double balance;
    private String currency;
    private AccountType type;
}
```

**Response DTO:**
```java
@Data
public class BankAccountResponseDTO {
    private String id;
    private Date cratedAt;
    private Double balance;
    private String currency;
    private AccountType type;
}
```

### AccountMapper 
Converts between BankAccount entities and DTOs for clean separation between layers.
```java
@Component
public class AccountMapper {

    public BankAccountResponseDTO fromBankAccount(BankAccount bankAccount){
        BankAccountResponseDTO bankAccountResponseDTO=new BankAccountResponseDTO();
        BeanUtils.copyProperties(bankAccount,bankAccountResponseDTO);
        return bankAccountResponseDTO;
    }
}
```
### AccountServiceImpl
Contains the business logic for creating and updating accounts.
Uses BankAccountRepository for persistence and AccountMapper for DTO conversion.
```java
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public BankAccountResponseDTO addAcount(BankAccountRequestDTO bankAccountRequestDTO) {
        BankAccount bankAccount=BankAccount.builder()
                .id(UUID.randomUUID().toString())
                .cratedAt(new Date())
                .balance(bankAccountRequestDTO.getBalance())
                .type(bankAccountRequestDTO.getType())
                .currency(bankAccountRequestDTO.getCurrency())
                .build();
       BankAccount saveBankAccount= bankAccountRepository.save(bankAccount);
       BankAccountResponseDTO bankAccountResponseDTO=accountMapper.fromBankAccount(saveBankAccount);
        return bankAccountResponseDTO;
    }
    @Override
    public BankAccountResponseDTO updateAccount(String id,BankAccountRequestDTO bankAccountRequestDTO) {
        BankAccount bankAccount=BankAccount.builder()
                .id(id)
                .cratedAt(new Date())
                .balance(bankAccountRequestDTO.getBalance())
                .type(bankAccountRequestDTO.getType())
                .currency(bankAccountRequestDTO.getCurrency())
                .build();
        BankAccount saveBankAccount= bankAccountRepository.save(bankAccount);
        BankAccountResponseDTO bankAccountResponseDTO=accountMapper.fromBankAccount(saveBankAccount);
        return bankAccountResponseDTO;
    }
}
```
### AccountRestController
Exposes REST endpoints to perform CRUD operations on bank accounts.
Handles requests like /api/bankAccounts using AccountService and repositories.
```java

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
```
### BankAccountGraphQlController
Provides GraphQL queries and mutations for the same bank account operations.
Uses annotations like @QueryMapping and @MutationMapping.
```java
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

```
### GraphQL Schema
```graphql
type Query{
    accountsList : [BankAccount],
    accountById (id:String): BankAccount,
    customers:[Customer]
}
type Mutation{
    addAccount(bankAccount : BankAccountDTO):BankAccount,
    updateAccount(id: String , bankAccount : BankAccountDTO):BankAccount,
    deleteAccount(id: String) :String
}

type BankAccount {
id : String,
creatdAt : Float,
balance : Float,
currency : String,
type : String,
customer : Customer
}
type Customer {
id : ID,
name : String,
bankAccounts:[BankAccount]
}
input BankAccountDTO{
balance : Float,
currency : String,
type : String
}

```
## Testing
### Testing with Postman
- **GET Request - Retrieve all accounts**
![GET Request](./images/getPost.png)
- **POST Request - Create new account**
  ![POST Reques](./images/postPost.png)
### Testing GraphQL with GraphiQL
- **Get all accounts**
  ![getGP](./images/getGP.png)
- **Create account**
  ![addGP](./images/addGP.png)
- **Update account** 
  ![upGP](./images/upGP.png)
- **Delete account**
  ![supGP](./images/supGP.png)
- **Get All Accounts with Customer**
  ![getAlGP](./images/getAlGP.png)
  
