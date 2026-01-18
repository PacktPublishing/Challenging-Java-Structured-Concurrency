package challenge.concurrency;

import challenge.concurrency.service.DefaultThreadsProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final DefaultThreadsProductService productService;

    public MainApplication(DefaultThreadsProductService productService) {
        this.productService = productService;
    }

    public static void main(String[] args) {
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            // test
            productService.reportProductsByLine("Motorcycles");           
        };
    }
}
