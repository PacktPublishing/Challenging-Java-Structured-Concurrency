package challenge.concurrency.service;

import challenge.concurrency.service.HealthService.Status;
import challenge.concurrency.service.UserService.User;
import static challenge.concurrency.ticket.Ticket.TICKET_ID;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class CheckingService {

    private static final Logger logger = Logger.getLogger(CheckingService.class.getName());

    private final UserService userService;
    private final ContractService contractService;
    private final HealthService healthService;
    private final EmailService emailService;

    public CheckingService(UserService userService, ContractService contractService,
            HealthService healthService, EmailService emailService) {
        this.userService = userService;
        this.contractService = contractService;
        this.healthService = healthService;
        this.emailService = emailService;
    }

    public void checkUserServices(String name) {

        User user = userService.findUser(name);
        
        Thread t1 = Thread.ofVirtual().start(() -> {
           Status internetStatus = healthService.checkInternetService(user, TICKET_ID.get());
            emailService.send(user, internetStatus, "Internet", TICKET_ID.get());
        });        
        Thread t2 = Thread.ofVirtual().start(() -> {
           Status mobileStatus = healthService.checkMobileService(user, TICKET_ID.get());
           emailService.send(user, mobileStatus, "Mobile", TICKET_ID.get());
        });        
        Thread t3 = Thread.ofVirtual().start(() -> {
           Status tvStatus = healthService.checkTVService(user, TICKET_ID.get());
           emailService.send(user, tvStatus, "TV", TICKET_ID.get());
        });

        // auxiliary checks that are not shared with the user        
        Thread t4 = Thread.ofVirtual().start(() -> {
            contractService.checkContractValidity(user, TICKET_ID.get());
        });
        Thread t5 = Thread.ofVirtual().start(() -> {
            contractService.checkUserPaymentsOnTime(user, TICKET_ID.get());
        });

        try { t1.join(); } catch (InterruptedException ex) {} // handle this
        try { t2.join(); } catch (InterruptedException ex) {}
        try { t3.join(); } catch (InterruptedException ex) {}
        try { t4.join(); } catch (InterruptedException ex) {}
        try { t5.join(); } catch (InterruptedException ex) {}
      
        logger.info(() -> "Request complete for ticket " + TICKET_ID.get());
    }
}
