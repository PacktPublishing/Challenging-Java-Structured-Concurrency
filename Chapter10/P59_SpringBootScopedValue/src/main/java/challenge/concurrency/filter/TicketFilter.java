package challenge.concurrency.filter;

import challenge.concurrency.ticket.Ticket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TicketFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(TicketFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        ScopedValue.where(Ticket.TICKET_ID, UUID.randomUUID().toString()).run(() -> {
            logger.info(() -> "Generated tickey id: " + Ticket.TICKET_ID.get());

            response.setHeader("Ticket-id", Ticket.TICKET_ID.get());

            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException ex) {
            }
        });
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {        

        return !request.getRequestURI().contains("/healthcheck/");
    }
}
