package challenge.concurrency.repository;

import challenge.concurrency.dto.Product;
import challenge.concurrency.dto.ProductDto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    
    Flux<ProductDto> findByLine(String line);
}
