package challenge.concurrency;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final Random rnd = new Random();
    private static final int LIMIT = 50;
    private static final int[] ELEMENTS
            = IntStream.generate(() -> rnd.nextInt(100)).limit(LIMIT).toArray();
    private static final int ELEMENT_TO_FIND = ELEMENTS[rnd.nextInt(LIMIT)];

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        

        logger.info(() -> "Array: " + Arrays.toString(ELEMENTS));      
        logger.info(() -> "Search for: " + ELEMENT_TO_FIND);
        
        Object index = FindCountedCompleter.findElement(ELEMENTS, ELEMENT_TO_FIND);

        if (index == null) {
            logger.info(() -> "Element " + ELEMENT_TO_FIND + " not found");
            // throw new NoSuchElementException();
        } else {
            logger.info(() -> "Element " + ELEMENT_TO_FIND + " found at index " + index);
        }
    }
}
