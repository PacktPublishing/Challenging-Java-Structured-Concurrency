package challenge.concurrency.service;

import challenge.concurrency.service.HealthService.Status;
import challenge.concurrency.service.UserService.User;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void send(User user, Status status, String service, String ticketId) {
        logger.info(() -> "TO: " + user.name() + "@gmail.com | SUBJECT: "
                + service + " status (" + ticketId + ") BODY: Status of " + service + " is " + status.statusType());
    }
}
