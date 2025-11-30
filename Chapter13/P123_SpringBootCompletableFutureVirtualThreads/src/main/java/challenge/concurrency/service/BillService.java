package challenge.concurrency.service;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

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

        var payBillsCf = supplyAsync(() -> userService.findUser(name), executor)
                .thenApplyAsync(user
                        -> supplyAsync(() -> bankAccountService.findBankAccount(user), executor)
                        .thenApplyAsync(bankAccount -> bankAccountService.validate(bankAccount), executor)
                        .thenAcceptAsync(validAccount -> allOf(
                        supplyAsync(() -> paymentService.payGasBill(user, validAccount), executor)
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Gas", transactionId), executor),
                        supplyAsync(() -> paymentService.payElectricityBill(user, validAccount), executor)
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Electricity", transactionId), executor),
                        supplyAsync(() -> paymentService.payWaterBill(user, validAccount), executor)
                                .thenAcceptAsync(transactionId -> emailService.send(user, "Water", transactionId), executor)
                ), executor), executor);

        payBillsCf.join();
    }
}
