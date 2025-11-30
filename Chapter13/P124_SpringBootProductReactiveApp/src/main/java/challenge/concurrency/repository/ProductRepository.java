package challenge.concurrency.repository;

import challenge.concurrency.vo.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    
    Flux<Product> findByLine(String line);
}
