package challenge.concurrency.ticket;

public class Ticket {
        
    public static final ScopedValue<String> TICKET_ID = ScopedValue.newInstance();
}
