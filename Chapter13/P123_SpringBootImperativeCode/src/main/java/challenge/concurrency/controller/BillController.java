package challenge.concurrency.controller;

import challenge.concurrency.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
public class BillController {
        
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }        
    
    @GetMapping("/{name}")
    public ResponseEntity<String> payBillsOfUser(@PathVariable String name) {
        
        billService.payBillOfUser(name);
                
        return ResponseEntity.ok("Dear " + name + ", you'll receive shortly more details on your e-mail");
    }
 }