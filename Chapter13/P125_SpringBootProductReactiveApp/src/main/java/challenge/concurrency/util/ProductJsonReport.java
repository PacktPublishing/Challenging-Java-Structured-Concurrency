package challenge.concurrency.util;

import challenge.concurrency.dto.ProductDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

public final class ProductJsonReport {

    private ProductJsonReport() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generate(String line, Flux<ProductDto> products) {

        Path reportPath = Paths.get("reports", "report_"
                + line + "_" + System.nanoTime() + "_" + Math.random() + ".txt");

        try {
            Files.createDirectories(reportPath.getParent());

            ObjectMapper mapper = new ObjectMapper();

            products.collectList()
                    .subscribe((List<ProductDto> ps) -> {
                        try {
                            mapper.writeValue(Files.newBufferedWriter(reportPath, StandardCharsets.UTF_8,
                                    StandardOpenOption.CREATE, StandardOpenOption.WRITE), ps);
                        } catch (IOException ex) {} // notify further that reports are having issues        
                    });
        } catch (IOException ex) {} // notify further that reports are having issues        
    }
}
