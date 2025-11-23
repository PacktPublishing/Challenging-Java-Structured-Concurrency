package challenge.concurrency;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        String[] f = {"F-1", "F-2", "F-3", "F-4", "F-5"};

        ThePhilosopher[] ps = {
            new ThePhilosopher(f[0], f[1]),
            new ThePhilosopher(f[1], f[2]),
            new ThePhilosopher(f[2], f[3]),
            new ThePhilosopher(f[3], f[4]),
            new ThePhilosopher(f[0], f[4]) // new ThePhilosopher(f[4], f[0]) - deadlock cause
        };

        Thread tps1 = Thread.ofVirtual().name("P-1").start(ps[0]);
        Thread tps2 = Thread.ofVirtual().name("P-2").start(ps[1]);
        Thread tps3 = Thread.ofVirtual().name("P-3").start(ps[2]);
        Thread tps4 = Thread.ofVirtual().name("P-4").start(ps[3]);
        Thread tps5 = Thread.ofVirtual().name("P-5").start(ps[4]);        

        tps1.join();
        tps2.join();
        tps3.join();
        tps4.join();
        tps5.join();
    }
}
