package challenge.concurrency.service;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import org.springframework.stereotype.Service;

@Service
public class BillService {
    
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final EmailService emailService;

    public BillService(UserService userService, BankAccountService bankAccountService,
            PaymentService paymentService, EmailService emailService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    public void payBillOfUser(String name) {

        var payBillsCf = supplyAsync(() -> userService.findUser(name))
                .thenApplyAsync(user
                        -> supplyAsync(() -> bankAccountService.findBankAccount(user))
                        .thenApplyAsync(bankAccount -> bankAccountService.validate(bankAccount))
                        .thenAcceptAsync(validAccount -> allOf(
                        supplyAsync(() -> paymentService.payGasBill(user, validAccount))
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Gas", transactionId)),
                        supplyAsync(() -> paymentService.payElectricityBill(user, validAccount))
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Electricity", transactionId)),
                        supplyAsync(() -> paymentService.payWaterBill(user, validAccount))
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Water", transactionId))
                )));
        
        payBillsCf.join();
    }
}