package challenge.concurrency.util;

import challenge.concurrency.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class ProductJsonReport {

    private ProductJsonReport() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generate(String line, List<ProductDto> products) {

        Path reportPath = Paths.get("reports", "report_" 
                + line + "_" + System.nanoTime() + "_" + Math.random() +  ".txt");
        
        try {
            Files.createDirectories(reportPath.getParent());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(Files.newBufferedWriter(reportPath, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE), products);
        } catch (IOException ex) {
        } // notify further that reports are having issues
    }
}
