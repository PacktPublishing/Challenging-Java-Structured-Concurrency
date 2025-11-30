package challenge.concurrency.controller;

import challenge.concurrency.service.CompletableFuturePlatformThreadsProductService;
import challenge.concurrency.service.CompletableFutureVirtualThreadsProductService;
import challenge.concurrency.service.DefaultThreadsProductService;
import challenge.concurrency.service.PlatformThreadsProductService;
import challenge.concurrency.service.VirtualThreadsProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ProductController {
    
    private final DefaultThreadsProductService defaultThreadsProductService;
    private final PlatformThreadsProductService platformThreadsProductService;
    private final VirtualThreadsProductService virtualThreadsProductService;
    private final CompletableFuturePlatformThreadsProductService completableFuturePlatformThreadsProductService;
    private final CompletableFutureVirtualThreadsProductService completableFutureVirtualThreadsProductService;

    public ProductController(DefaultThreadsProductService defaultThreadsProductService, 
            PlatformThreadsProductService platformThreadsProductService, 
            VirtualThreadsProductService virtualThreadsProductService,
            CompletableFuturePlatformThreadsProductService completableFuturePlatformThreadsProductService,
            CompletableFutureVirtualThreadsProductService completableFutureVirtualThreadsProductService) {
        this.defaultThreadsProductService = defaultThreadsProductService;
        this.platformThreadsProductService = platformThreadsProductService;
        this.virtualThreadsProductService = virtualThreadsProductService;
        this.completableFuturePlatformThreadsProductService = completableFuturePlatformThreadsProductService;
        this.completableFutureVirtualThreadsProductService = completableFutureVirtualThreadsProductService;
    }  
    
    @GetMapping("/{line}")
    public ResponseEntity<String> generateDefaultProductReport(@PathVariable String line) {
        
        defaultThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (default) report for line " + line);
    }
    
    @GetMapping("/pt/{line}")
    public ResponseEntity<String> generatePlatformProductReport(@PathVariable String line) {
        
        platformThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (platform) report for line " + line);
    }
    
    @GetMapping("/vt/{line}")
    public ResponseEntity<String> generateVirtualProductReport(@PathVariable String line) {
        
        virtualThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (virtual) report for line " + line);
    }
    
    @GetMapping("/cf/pt/{line}")
    public ResponseEntity<String> generateCompletableFuturePlatformProductReport(@PathVariable String line) {
        
        completableFuturePlatformThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (CompletableFuture + platform) report for line " + line);
    }
    
    @GetMapping("/cf/vt/{line}")
    public ResponseEntity<String> generateCompletableFutureVirtualProductReport(@PathVariable String line) {
        
        completableFutureVirtualThreadsProductService.reportProductsByLine(line);
        return ResponseEntity.ok("Generating (CompletableFuture + virtual) report for line " + line);
    }
}
