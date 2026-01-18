package challenge.concurrency.controller;

import challenge.concurrency.service.StockPriceService;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/stocks")
public class StockPriceController {

    private static final Logger logger = Logger.getLogger(StockPriceController.class.getName());
    
    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    // http://localhost:8080/stocks/stream
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamStockPrices() {

        SseEmitter emitter = new SseEmitter();
        ScheduledExecutorService executor 
                = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());        

        executor.scheduleAtFixedRate(() -> {
            try {              
                emitter.send(stockPriceService.streamOfStockPrice());
            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

        emitter.onCompletion(() -> {
            logger.info("Emitter completed ...");
            executor.shutdown();
        });
        
        emitter.onTimeout(() -> {
            logger.warning("Emitter timeout ...");
            emitter.complete();
            executor.shutdown();
        });
        
        emitter.onError((e) -> {
            logger.severe(() -> "Emitter error: " + e.getMessage());
            executor.shutdown();
        });

        return emitter;
    }
}
