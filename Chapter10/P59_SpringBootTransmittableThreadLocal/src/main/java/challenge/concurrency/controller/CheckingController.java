package challenge.concurrency.controller;

import challenge.concurrency.service.CheckingService;
import static challenge.concurrency.ticket.Ticket.TICKET_ID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class CheckingController {
        
    private final CheckingService checkingService;

    public CheckingController(CheckingService checkingService) {
        this.checkingService = checkingService;
    }        
    
    @GetMapping("/{name}")
    public ResponseEntity<String> checkUserServices(@PathVariable String name) {
        
        checkingService.checkUserServices(name);
                
        return ResponseEntity.ok("Dear " + name + " your request was registered with ticket #" 
                + TICKET_ID.get() + ". You'll receive shortly more details on your e-mail.");
    }
 }