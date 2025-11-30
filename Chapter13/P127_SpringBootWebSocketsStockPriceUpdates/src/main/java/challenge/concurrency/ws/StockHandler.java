package challenge.concurrency.ws;

import challenge.concurrency.service.StockPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class StockHandler extends TextWebSocketHandler {

    private static final Logger logger = Logger.getLogger(StockHandler.class.getName());

    private static final ObjectMapper objectMapper
            = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private StockPriceService stockPriceService;

    private ScheduledExecutorService executor;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        executor = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        String msg = message.getPayload();

        if (msg.equals("send")) {

            logger.info("New connection ... start sending stocks updates!");

            executor.scheduleAtFixedRate(() -> {
                try {
                    session.sendMessage(new TextMessage(
                            objectMapper.writeValueAsString(stockPriceService.streamOfStockPrice())));
                } catch (IOException e) {
                    logger.severe(() -> "Exception: " + e);
                    executor.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        executor.shutdown();
    }
}
