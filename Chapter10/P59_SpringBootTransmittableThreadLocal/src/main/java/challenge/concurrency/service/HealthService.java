package challenge.concurrency.service;

import challenge.concurrency.service.UserService.User;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    private static final Logger logger = Logger.getLogger(HealthService.class.getName());

    enum StatusType { OK, WARNING, NOT_WORKING }
    public record Status(StatusType statusType) {}

    public Status checkInternetService(User user, String ticketId) {

        logger.info(() -> "Checking internet service for " + user + " | ticket " + ticketId);
        
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}

        return new Status(StatusType.OK); // or, WARNING, NOT_WORKING
    }

    public Status checkMobileService(User user, String ticketId) {

        logger.info(() -> "Checking mobile service for " + user + " | ticket " + ticketId);
        
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}

        return new Status(StatusType.OK); // or, WARNING, NOT_WORKING
    }

    public Status checkTVService(User user, String ticketId) {

        logger.info(() -> "Checking TV service for " + user + " | ticket " + ticketId);
        
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}

        return new Status(StatusType.OK); // or, WARNING, NOT_WORKING
    }
}
