package challenge.concurrency;

public class Main {

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        WeatherMap wm = new WeatherMap();
        
        for (int i = 1; i <= 5; i++) { // 5 days     
            try {
                wm.buildWeatherMap(i);
            } catch (InterruptedException ex) {}
        }
    }
}
