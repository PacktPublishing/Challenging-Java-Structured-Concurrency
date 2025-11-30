package challenge.concurrency.data;

import java.time.Instant;

public class StockPrice {

    private String symbol;
    private double price;
    private Instant timestamp;

    public StockPrice(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = Instant.now();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
