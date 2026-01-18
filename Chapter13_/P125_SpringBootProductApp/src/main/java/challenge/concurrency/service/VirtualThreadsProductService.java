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
public class VirtualThreadsProductService {

    private static final Logger logger = Logger.getLogger(VirtualThreadsProductService.class.getName());

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final ProductRepository productRepository;

    public VirtualThreadsProductService(ProductRepository productRepository) {
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
