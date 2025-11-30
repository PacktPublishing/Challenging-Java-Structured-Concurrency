package challenge.concurrency;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class FindCountedCompleter extends CountedCompleter {
    
    private static final Logger logger = Logger.getLogger(FindCountedCompleter.class.getName());
    
    private static final int THRESHOLD = 2;

    private final int[] array;
    private final int left;
    private final int right;   
    private final int toFind;
    private final AtomicReference<Integer> found;    

    public FindCountedCompleter(CountedCompleter<?> completer, int[] array, 
            AtomicReference<Integer> found, int left, int right, int toFind) {
        super(completer);
        this.array = array;
        this.found = found;
        this.left = left;
        this.right = right;
        this.toFind = toFind;
    }

    @Override
    public void compute() {

        int copyLeft = left, copyRight = right;

        while (found.get() == null && copyRight >= copyLeft) {

            if ((copyRight - copyLeft) >= THRESHOLD) {
                int middle = (copyLeft + copyRight) >>> 1;

                addToPendingCount(1);            

                new FindCountedCompleter(this, array, found, middle, copyRight, toFind).fork();
                copyRight = middle;
            } else {
                int element = array[left];

                if (find(element) && found.compareAndSet(null, left)) {
                    
                    quietlyCompleteRoot(); 
                    
                    logger.info(() -> "Found and set element " + element + " at index " + left);
                }               
                break;
            }
        }
        
        if (found.get() == null) { 
            propagateCompletion(); 
        }
    }      

    @Override
    public Integer getRawResult() {        
        return found.get();
    }
    
    boolean find(int element) {        
        return element == toFind;
    }  
    
    public static Object findElement(int[] array, int toFind) {
        return new FindCountedCompleter(null, array, new AtomicReference<>(), 
                0, array.length, toFind).invoke();
    }
}