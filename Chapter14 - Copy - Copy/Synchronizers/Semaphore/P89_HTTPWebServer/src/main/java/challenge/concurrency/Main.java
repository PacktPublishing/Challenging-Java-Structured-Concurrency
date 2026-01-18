package challenge.concurrency;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int PLATFORM_THREADS = 300;
    private static final int HTTP_WEB_PORT = 8005;
    
    public static void main(String[] args) throws IOException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        initAndStartWS(true, true);                           
    }
    
    private static void initAndStartWS(boolean useVirtualThreads, boolean useLocking) throws IOException {
        
        HttpServer httpWS = HttpServer.create(new InetSocketAddress(HTTP_WEB_PORT), 0);
        
        httpWS.createContext("/hws", new HttpWSHandler(useLocking));
        
        if (useVirtualThreads) {
            httpWS.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        } else {
            httpWS.setExecutor(Executors.newFixedThreadPool(PLATFORM_THREADS));
        }
        
        httpWS.start();
        
        logger.info(() -> " Server is ready on port " 
                + HTTP_WEB_PORT + " | http://localhost:8005/hws");
    }
}
