package challenge.concurrency.service;

import challenge.concurrency.dto.ProductDto;
import java.util.List;
import org.springframework.stereotype.Service;
import challenge.concurrency.repository.ProductRepository;
import challenge.concurrency.util.ProductJsonReport;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class CompletableFutureVirtualThreadsProductService {

    private static final Logger logger = Logger.getLogger(CompletableFutureVirtualThreadsProductService.class.getName());
   
    private final ProductRepository productRepository;

    public CompletableFutureVirtualThreadsProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void reportProductsByLine(String line) {

        CompletableFuture.runAsync(() -> {
            logger.info(() -> "Report: " + line + " | Thread: " + Thread.currentThread());

            List<ProductDto> products = productRepository.findByLine(line);
            ProductJsonReport.generate(line, products);
        }, Thread::startVirtualThread);
    }
}
