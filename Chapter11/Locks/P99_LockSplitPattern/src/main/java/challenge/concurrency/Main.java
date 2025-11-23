package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CollaborativeEditor editor = new CollaborativeEditor();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 20; i++) {
                double rnd = Math.random();

                if (rnd < 0.2d) {
                    executor.submit(() -> editor.insertCharacter(""));
                }

                if (rnd >= 0.0d && rnd < 0.4d) {
                    executor.submit(() -> editor.deleteCharacter(""));
                }
                
                if (rnd >= 0.4d && rnd < 0.6d) {
                    executor.submit(() -> editor.alignmentRight(0));
                }

                if (rnd >= 0.6d && rnd < 0.8d) {
                    executor.submit(() -> editor.addColumnInTable(""));
                }

                if (rnd >= 0.8d && rnd < 1.0d) {
                    executor.submit(() -> editor.removeColumnInTable(""));
                }
            }
        }
    }
}
