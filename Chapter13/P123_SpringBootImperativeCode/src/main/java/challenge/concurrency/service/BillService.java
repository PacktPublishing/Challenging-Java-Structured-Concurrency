package challenge.concurrency.service;

import challenge.concurrency.service.BankAccountService.BankAccount;
import challenge.concurrency.service.PaymentService.TransactionId;
import challenge.concurrency.service.UserService.User;
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

        User user = userService.findUser(name);
        
        BankAccount bankAccount = bankAccountService.findBankAccount(user);        
        BankAccount validAccount = bankAccountService.validate(bankAccount);
        
        TransactionId transactionId = paymentService.payGasBill(user, validAccount);
        emailService.send(user, "Gas", transactionId);
        transactionId = paymentService.payElectricityBill(user, validAccount);
        emailService.send(user, "Electricity", transactionId);
        transactionId = paymentService.payWaterBill(user, validAccount);
        emailService.send(user, "Water", transactionId);       
    }
}