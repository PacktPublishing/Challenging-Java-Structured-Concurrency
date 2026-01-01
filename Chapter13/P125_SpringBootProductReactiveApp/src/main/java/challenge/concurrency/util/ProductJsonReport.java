package challenge.concurrency.util;

import challenge.concurrency.vo.Product;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import reactor.core.publisher.Flux;

public final class ProductJsonReport {

    private ProductJsonReport() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generate(String line, Flux<Product> products) {

        Path reportPath = Paths.get("reports", "report_" 
                + line + "_" + System.nanoTime() + "_" + Math.random() + ".txt");

        try {
            Files.createDirectories(reportPath.getParent());

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

            products.collectList()
                    .subscribe((List<Product> ps) -> {
                        try {
                            mapper.writeValue(Files.newBufferedWriter(reportPath, StandardCharsets.UTF_8,
                                    StandardOpenOption.CREATE, StandardOpenOption.WRITE), ps);
                        } catch (IOException ex) {} // notify further that reports are having issues        
            });
        } catch (IOException ex) {} // notify further that reports are having issues        
    }
}
