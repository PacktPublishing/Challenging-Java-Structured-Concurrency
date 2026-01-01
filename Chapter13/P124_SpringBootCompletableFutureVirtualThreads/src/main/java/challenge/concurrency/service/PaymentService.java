package challenge.concurrency.service;

import challenge.concurrency.service.BankAccountService.BankAccount;
import challenge.concurrency.service.UserService.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public record TransactionId(long id) {}

    public TransactionId payGasBill(User user, BankAccount account) {
        return new TransactionId((long) (Math.random() * 10000));
    }

    public TransactionId payElectricityBill(User user, BankAccount account) {
        return new TransactionId((long) (Math.random() * 10000));
    }

    public TransactionId payWaterBill(User user, BankAccount account) {
        return new TransactionId((long) (Math.random() * 10000));
    }
}