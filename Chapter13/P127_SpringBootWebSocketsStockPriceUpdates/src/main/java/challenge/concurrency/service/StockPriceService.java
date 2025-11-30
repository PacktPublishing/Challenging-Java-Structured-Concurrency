package challenge.concurrency.service;

import challenge.concurrency.data.StockPrice;
import org.springframework.stereotype.Service;

@Service
public class StockPriceService {

    private static final String[] SYMBOL = {"GOOGL", "AMZN", "TSLA", "AAPL", "MSFT"};

    public StockPrice streamOfStockPrice() {

        return new StockPrice(randomSymbol(), randomPrice());
    }

    private double randomPrice() {
        return Math.round(100 + (Math.random() * 100) * 5.0) / 100.0;
    }

    private String randomSymbol() {
        return SYMBOL[(int) (Math.random() * SYMBOL.length)];
    }
}
