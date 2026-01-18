package challenge.concurrency.service;

import challenge.concurrency.service.HealthService.Status;
import challenge.concurrency.service.UserService.User;
import static challenge.concurrency.ticket.Ticket.TICKET_ID;
import com.alibaba.ttl.threadpool.TtlExecutors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class CheckingService {

    private static final Logger logger = Logger.getLogger(CheckingService.class.getName());

    private static final ExecutorService executor 
            = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(10));

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

        Future<?> f1 = executor.submit(() -> {
            Status internetStatus = healthService.checkInternetService(user, TICKET_ID.get());
            emailService.send(user, internetStatus, "Internet", TICKET_ID.get());
        });
        Future<?> f2 = executor.submit(() -> {
            Status mobileStatus = healthService.checkMobileService(user, TICKET_ID.get());
            emailService.send(user, mobileStatus, "Mobile", TICKET_ID.get());
        });
        Future<?> f3 = executor.submit(() -> {
            Status tvStatus = healthService.checkTVService(user, TICKET_ID.get());
            emailService.send(user, tvStatus, "TV", TICKET_ID.get());
        });

        // auxiliary checks that are not shared with the user        
        Future<?> f4 = executor.submit(() -> {
            contractService.checkContractValidity(user, TICKET_ID.get());
        });
        Future<?> f5 = executor.submit(() -> {
            contractService.checkUserPaymentsOnTime(user, TICKET_ID.get());
        });

        try { f1.get(); } catch (InterruptedException | ExecutionException ex) {} // handle this
        try { f2.get(); } catch (InterruptedException | ExecutionException ex) {}
        try { f3.get(); } catch (InterruptedException | ExecutionException ex) {}
        try { f4.get(); } catch (InterruptedException | ExecutionException ex) {}
        try { f5.get(); } catch (InterruptedException | ExecutionException ex) {}

        logger.info(() -> "Request complete for ticket " + TICKET_ID.get());
    }
}
