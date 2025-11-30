package challenge.concurrency.controller;

import challenge.concurrency.service.ReactiveThreadsProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ProductController {
    
    private final ReactiveThreadsProductService reactiveThreadsProductService;
  
    public ProductController(ReactiveThreadsProductService reactiveThreadsProductService) {
        this.reactiveThreadsProductService = reactiveThreadsProductService;       
    }  
    
    @GetMapping("/reactive/{line}")
    public ResponseEntity<String> generateReactiveProductReport(@PathVariable String line) {
        
        reactiveThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (reactive) report for line " + line);
    }        
}
