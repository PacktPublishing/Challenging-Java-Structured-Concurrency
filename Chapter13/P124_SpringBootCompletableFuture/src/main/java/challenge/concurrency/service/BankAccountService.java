package challenge.concurrency.service;

import challenge.concurrency.service.UserService.User;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
   
    public record BankAccount() {}

    public BankAccount findBankAccount(User user) {
        return new BankAccount();
    }

    public BankAccount validate(BankAccount account) {
        return account;
    }
}