package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    private static final int MAX_ELEMENTS = 50;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        List<Integer> intlist = new ArrayList<>();
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            intlist.add(rnd.nextInt(10) + 1);
        }

        long totalSum = RecursiveScopedTask.execute(intlist, 0, intlist.size());
        
        logger.info(() -> "Total sum: " + totalSum);
    }
}
