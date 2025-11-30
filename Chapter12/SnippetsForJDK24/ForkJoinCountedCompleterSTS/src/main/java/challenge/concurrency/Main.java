package challenge.concurrency;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Random rnd = new Random();
    private static final int LIMIT = 50;
    private static final int[] ELEMENTS
            = IntStream.generate(() -> rnd.nextInt(100)).limit(LIMIT).toArray();
    private static final int ELEMENT_TO_FIND = ELEMENTS[rnd.nextInt(LIMIT)];

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
       
        logger.info(() -> "Array: " + Arrays.toString(ELEMENTS));      
        logger.info(() -> "Search for: " + ELEMENT_TO_FIND);               
        
        try (StructuredTaskScope scope = new StructuredTaskScope.ShutdownOnSuccess<>()) {

            Subtask<Integer>[] subtasks = new StructuredTaskScope.Subtask[ELEMENTS.length];

            for (int i = 0; i < ELEMENTS.length; i++) {
                int index = i;
                subtasks[index] = scope.fork(() -> find(ELEMENTS[index], index));
            }

            scope.join();
                     
            Optional<Subtask<Integer>> found = Stream.of(subtasks)
                    .filter(s -> s.state().equals(State.SUCCESS))
                    .findFirst();

            logger.info(() -> "Found element " + ELEMENT_TO_FIND+ " at index " + found.orElseThrow().get()); 
        }
    }

    static int find(int element, int index) {
       
        if (element == ELEMENT_TO_FIND) {
            return index;
        } else {
            throw new NoSuchElementException();
        }
    }
}