package challenge.concurrency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int LINE_NR = 500;

    private static class LinesFileWriter implements Runnable {

        @Override
        public void run() {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("lines.txt"))) {

                for (int i = 0; i < LINE_NR; i++) {

                    if (Thread.currentThread().isInterrupted()) {
                        logger.log(Level.WARNING, "File writing interrupted at line {0}. Closing file...", i);
                        throw new InterruptedException("File writing interrupted");
                    }

                    bw.write("Line number " + i + "\n");                    
                }
            } catch (IOException | InterruptedException e) {
                logger.severe(() -> "Exception: " + e);

                try {
                    // Clean up resources (delete the incomplete file)
                    logger.info("Deleting the corrupted (incomplete) file ...");
                    Files.deleteIfExists(Path.of("lines.txt"));
                    logger.info("Deletion done.");
                } catch (IOException ex) {
                    logger.severe(() -> "Exception: " + e);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread thread = Thread.ofVirtual().name("vt").start(new LinesFileWriter());       

        Thread.sleep(5);    // Write for a while
        thread.interrupt(); // Interrupt the file writing operation

        thread.join();
    }
}
