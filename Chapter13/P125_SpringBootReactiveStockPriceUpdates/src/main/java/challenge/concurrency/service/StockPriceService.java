package challenge.concurrency.service;

import challenge.concurrency.data.StockPrice;
import java.time.Duration;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

@Service
public class StockPriceService {
    
    private static final Logger logger = Logger.getLogger(StockPriceService.class.getName());

    private static final String[] SYMBOL = {"GOOGL", "AMZN", "TSLA", "AAPL", "MSFT"};
    
    public Flux<StockPrice> streamOfStockPrice() {

        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> new StockPrice(randomSymbol(), randomPrice()))
                .doOnError(ex -> logger.severe(() -> "Error: " + ex.getMessage()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    private double randomPrice() {
        return Math.round(100 + (Math.random() * 100) * 5.0) / 100.0;
    }
    
    private String randomSymbol() {        
        return SYMBOL[(int) (Math.random() * SYMBOL.length)];
    }
}