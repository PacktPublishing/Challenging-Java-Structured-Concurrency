package challenge.concurrency;

import java.util.concurrent.Exchanger;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int TANK_APPROX_CAPACITY = 500;
    private static final Exchanger<Integer> exchanger = new Exchanger<>();

    private static class PumpUp implements Runnable {

        private int tank;

        @Override
        public void run() {

            while (true) {
                try {
                    while (tank < TANK_APPROX_CAPACITY) {
                        int gasoline = (int) (Math.random() * 100);
                        Thread.sleep((int) (Math.random() * 1000));

                        tank = tank + gasoline;
                        logger.info(() -> "PumpUp: Pumped up " + gasoline 
                                + " tons of gasoline ...remaining approx. " 
                                + (TANK_APPROX_CAPACITY - tank) + " tons to pump up");                        
                    }

                    logger.info("PumpUp: Waiting for PumpDown to empty the tank ...");
                    tank = exchanger.exchange(tank);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Exception: " + ex);
                    break;
                }
            }
        }
    }

    private static class PumpDown implements Runnable {

        private int tank;

        @Override
        public void run() {

            while (true) {
                try {
                    logger.info("PumpDown: Waiting for PumpUp to fill up the tank ...");
                    tank = exchanger.exchange(tank);

                    logger.info(() -> "PumpDown: Tank is full with " + tank + " tons ...");

                    while (tank > 0) {
                        int gasoline = (int) (Math.random() * 200);
                        Thread.sleep((int) (Math.random() * 5000));
                        
                        tank = tank - gasoline;
                        logger.info(() -> "PumpDown: Pumped down " + gasoline + " tons ... remaining " 
                                + tank + " tons to pump down");                        
                    }                   
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Exception: " + ex);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread t1 = Thread.ofVirtual().start(new PumpUp());
        Thread t2 = Thread.ofVirtual().start(new PumpDown());
        
        t1.join();
        t2.join();
    }
}
