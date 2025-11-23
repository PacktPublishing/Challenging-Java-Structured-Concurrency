package challenge.concurrency;

public class Account {
    
    private int amount;
    
    public Account(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
    
    public int deposit(int value) {
        amount = amount + value;       
        
        return amount;
    }
    
    public int withdraw(int value) {
        amount = amount - value;        
        
        return amount;
    }
}
