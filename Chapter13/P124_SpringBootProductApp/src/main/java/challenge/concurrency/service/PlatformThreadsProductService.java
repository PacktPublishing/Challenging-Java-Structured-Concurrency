package challenge.concurrency.service;

import challenge.concurrency.dto.ProductDto;
import java.util.List;
import org.springframework.stereotype.Service;
import challenge.concurrency.repository.ProductRepository;
import challenge.concurrency.util.ProductJsonReport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
public class PlatformThreadsProductService {

    private static final Logger logger = Logger.getLogger(PlatformThreadsProductService.class.getName());

    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // using platform threads

    private final ProductRepository productRepository;

    public PlatformThreadsProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void reportProductsByLine(String line) {

        executor.execute(() -> {
            logger.info(() -> "Report: " + line + " | Thread: " + Thread.currentThread());

            List<ProductDto> products = productRepository.findByLine(line);
            ProductJsonReport.generate(line, products);
        });
    }
}
