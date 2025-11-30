package challenge.concurrency.service;

import challenge.concurrency.service.PaymentService.TransactionId;
import challenge.concurrency.service.UserService.User;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void send(User user, String bill, TransactionId transactionId) {
        logger.info(() -> "User: " + user + " Bill: " + bill + " Transaction ID: #" + transactionId);
    }
}