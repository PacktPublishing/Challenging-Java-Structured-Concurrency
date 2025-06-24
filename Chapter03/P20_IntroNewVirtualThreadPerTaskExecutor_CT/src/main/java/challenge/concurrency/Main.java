package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int TASK_NR = 5;

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> {
            logger.info(Thread.currentThread().toString());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            logger.info(() -> "Task successfully executed by thread #" 
                    + Thread.currentThread().threadId());
        };

        VirtualExecutor vExecutor = new VirtualExecutor();
        for (int i = 0; i < TASK_NR; i++) {

            vExecutor.execute(voidTask);            
        }        
    }
}