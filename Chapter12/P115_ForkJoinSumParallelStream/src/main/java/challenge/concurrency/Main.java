package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    private static final int MAX_ELEMENTS = (Integer.MAX_VALUE/14);

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<Integer> intlist = new ArrayList<>();
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            intlist.add(rnd.nextInt(10) + 1);
        }
        
        // sequential for
        int result = 0;
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            result += intlist.get(i);
        }
        int result1 = result;
        logger.info(() -> "Sequential for: " + result1);

        // sequential stream
        int result2 = intlist.stream().reduce(0, Integer::sum);
        logger.info(() -> "Parallel stream: " + result2);

        // parallel stream
        int result3 = intlist.parallelStream().reduce(0, Integer::sum);      
        logger.info(() -> "Sequential stream: " + result3);       
    }
    
    
}
