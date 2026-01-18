package challenge.concurrency.ticket;

public class Ticket {
        
    public static final InheritableThreadLocal<String> TICKET_ID = new InheritableThreadLocal<>();
}
