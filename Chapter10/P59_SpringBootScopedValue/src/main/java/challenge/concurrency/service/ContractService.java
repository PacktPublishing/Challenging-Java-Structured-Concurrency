package challenge.concurrency.service;

import challenge.concurrency.service.UserService.User;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    
    private static final Logger logger = Logger.getLogger(ContractService.class.getName());           

    public boolean checkContractValidity(User user, String ticketId) {
        
        logger.info(() -> "Checking contract validity of " + user + " | ticket " + ticketId);
        
        try { Thread.sleep((long) (Math.random() * 3000)); } catch (InterruptedException ex) {}
        
        return true; // or, false
    }    
    
    public boolean checkUserPaymentsOnTime(User user, String ticketId) {
        
        logger.info(() -> "Checking if payments are on time for " + user + " | ticket " + ticketId);
        
        try { Thread.sleep((long) (Math.random() * 3000)); } catch (InterruptedException ex) {}
        
        return true; // or, false
    }
}