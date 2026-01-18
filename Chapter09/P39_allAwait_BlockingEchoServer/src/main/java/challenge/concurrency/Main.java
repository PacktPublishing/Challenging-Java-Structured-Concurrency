package challenge.concurrency;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final int SERVER_PORT = 4444;
    private static final String SERVER_IP = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException {
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
      
        // open a brand new server socket channel
        try (ServerSocketChannel serverSC = ServerSocketChannel.open()) {

            // server socket channel was created
            if (serverSC.isOpen()) {

                // configure the blocking mode
                serverSC.configureBlocking(true);

                // optionally, configure the server side options
                serverSC.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                serverSC.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                // bind the server socket channel to local address
                serverSC.bind(new InetSocketAddress(SERVER_IP, SERVER_PORT));

                // waiting for clients
                logger.info("Waiting for clients ...");

                try (var scope = StructuredTaskScope.open(Joiner.<SocketChannel>awaitAll())) {

                    // ready to accept incoming connections
                    while (true) {
                        SocketChannel acceptSC = serverSC.accept();
                        scope.fork(() -> messageEcho(acceptSC, ByteBuffer.allocateDirect(1024)));
                    }
                }
            } else {
                logger.warning("Server socket channel unavailable!");
            }
        } catch (IOException ex) {
            logger.severe(ex.toString());
            // handle exception
        }
    }

    public static void messageEcho(SocketChannel acceptSC, ByteBuffer tBuffer) {

        try {
            SocketAddress addr = acceptSC.getRemoteAddress();
            logger.info(() -> "New connection: " + addr
                    + " handled by " + Thread.currentThread());

            // sending data
            while (acceptSC.read(tBuffer) != -1) {

                tBuffer.flip();

                acceptSC.write(tBuffer);

                if (tBuffer.hasRemaining()) {
                    tBuffer.compact();
                } else {
                    tBuffer.clear();
                }
            }
        } catch (IOException ex) {} // handle exception
    }
}
