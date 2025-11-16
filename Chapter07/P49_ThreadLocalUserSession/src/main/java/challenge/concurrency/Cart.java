package challenge.concurrency;

import java.util.List;

public class Cart {
    
    private final String user;
    private List<String> items;

    public Cart(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }   
    
    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
    
    public void addItem(String item) {
        this.items.add(item);
    }
    
    public void removeItem(String item) {
        this.items.remove(item);
    }

    @Override
    public String toString() {
        return "Cart{" + "user=" + user + ", items=" + items + '}';
    }         
}
