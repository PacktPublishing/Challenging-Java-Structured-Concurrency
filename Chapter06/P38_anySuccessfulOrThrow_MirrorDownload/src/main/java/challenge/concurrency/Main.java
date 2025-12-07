package challenge.concurrency;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final List<URI> mirrors = List.of(
            URI.create("https://foo-mirror.com"),
            URI.create("https://buzz-mirror.com"),
            URI.create("https://bizz-mirror.com"));

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        File file = new File("kaboom.js");
        download(file, mirrors);
        
        logger.info(() -> "File was downloaded: " + file.getAbsolutePath());
    }

    private static File download(File file, List<URI> mirrors) throws IOException, InterruptedException {
        
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<InputStream>anySuccessfulOrThrow())) {

            for (URI mirror : mirrors) {
                scope.fork(() -> fileFromMirror(mirror));
            }

            try (InputStream in = scope.join()) {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            
            return file;
        }
    }

    private static InputStream fileFromMirror(URI uri) throws InterruptedException {
        
        Thread.sleep((long) (Math.random() * 5000));        
        String fileContent = "kaboom.js / " + uri;
        
        return new ByteArrayInputStream(fileContent.getBytes());
    }
}
