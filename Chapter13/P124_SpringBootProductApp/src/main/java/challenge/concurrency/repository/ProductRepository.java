package challenge.concurrency.repository;

import challenge.concurrency.dto.ProductDto;
import java.util.List;
import challenge.concurrency.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<ProductDto> findByLine(String line);
}
