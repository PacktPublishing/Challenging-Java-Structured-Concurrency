package challenge.concurrency.ticket;

import com.alibaba.ttl.TransmittableThreadLocal;

public class Ticket {
        
    public static final TransmittableThreadLocal<String> TICKET_ID = new TransmittableThreadLocal<>();
}
