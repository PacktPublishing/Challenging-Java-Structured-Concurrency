package challenge.concurrency;

import java.lang.Thread.Builder.OfVirtual;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the virtual thread
        Runnable voidTask = () -> logger.info(
                () -> "I'm a virtual thread: " + Thread.currentThread().toString());
        
        // creating virtual threads via OfVirtual 
        OfVirtual virtualBuilder = Thread.ofVirtual(); 
        
        // here, set the properties common to all threads created via this virtual thread builder
                
        logger.info("Before starting the virtual threads (1 and 2) ...");                                                                                 
        
        Thread virtualThread1 = virtualBuilder.start(voidTask);
        Thread virtualThread2 = virtualBuilder.start(voidTask);                        

        virtualThread1.join();        
        virtualThread2.join();        

        logger.info("After the virtual threads (1 and 2) has terminated ...");
        
        logger.info("--------------------------");
        
        logger.info("Before starting the virtual threads (3 and 4) ...");                                                                                         
        // chaining OfVirtual in a single line of code (this is similar to startVirtualThread())       
        Thread virtualThread3 = Thread.ofVirtual().start(voidTask);
        Thread virtualThread4 = Thread.ofVirtual().start(voidTask);                

        virtualThread3.join();        
        virtualThread4.join();        

        logger.info("After the virtual threads (3 and 4) has terminated ...");     
        
        logger.info("--------------------------");
        
        // creating unstarted virtual threads
        logger.info("Before creating the unstarted virtual threads (5, 6, 7, and 8) ...");
        
        Thread virtualThread5 = virtualBuilder.unstarted(voidTask);
        Thread virtualThread6 = virtualBuilder.unstarted(voidTask);
        
        Thread virtualThread7 = Thread.ofVirtual().unstarted(voidTask);
        Thread virtualThread8 = Thread.ofVirtual().unstarted(voidTask);
        
        logger.info("Before starting the unstarted virtual threads (5, 6, 7, and 8) ...");
        
        logger.info( () -> "virtualThread5 is alive ? " + virtualThread5.isAlive() 
                + " | virtualThread6 is alive ? " + virtualThread6.isAlive()
                + " | virtualThread7 is alive ? " + virtualThread7.isAlive()
                + " | virtualThread8 is alive ? " + virtualThread8.isAlive());
        
        // start the virtual threads
        virtualThread5.start();
        virtualThread6.start();
        virtualThread7.start();
        virtualThread8.start();
                        
        logger.info("After starting the virtual threads (5, 6, 7, and 8) ...");
        
        // you'll see "true" only for those threads that haven't complete yet
        logger.info( () -> "virtualThread5 is alive ? " + virtualThread5.isAlive() 
                + " | virtualThread6 is alive ? " + virtualThread6.isAlive()
                + " | virtualThread7 is alive ? " + virtualThread7.isAlive()
                + " | virtualThread8 is alive ? " + virtualThread8.isAlive());        
        
        virtualThread5.join();
        virtualThread6.join();
        virtualThread7.join();
        virtualThread8.join();    
        
        logger.info("After the virtual threads (5, 6, 7, and 8) has terminated ...");     
        logger.info( () -> "virtualThread5 is alive ? " + virtualThread5.isAlive() 
                + " | virtualThread6 is alive ? " + virtualThread6.isAlive()
                + " | virtualThread7 is alive ? " + virtualThread7.isAlive()
                + " | virtualThread8 is alive ? " + virtualThread8.isAlive());
    }
}