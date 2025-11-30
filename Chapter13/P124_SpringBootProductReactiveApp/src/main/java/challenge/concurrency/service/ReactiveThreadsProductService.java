package challenge.concurrency.service;

import org.springframework.stereotype.Service;
import challenge.concurrency.repository.ProductRepository;
import challenge.concurrency.util.ProductJsonReport;
import challenge.concurrency.vo.Product;
import java.util.logging.Logger;
import reactor.core.publisher.Flux;

@Service
public class ReactiveThreadsProductService {

    private static final Logger logger = Logger.getLogger(ReactiveThreadsProductService.class.getName());

    private final ProductRepository productRepository;

    public ReactiveThreadsProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void reportProductsByLine(String line) {

        logger.info(() -> "Report: " + line + " | Thread: " + Thread.currentThread());

        Flux<Product> productsFlux = productRepository.findByLine(line);
        
        ProductJsonReport.generate(line, productsFlux);
    }
}
