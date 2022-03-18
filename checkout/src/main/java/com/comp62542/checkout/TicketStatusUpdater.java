package com.comp62542.checkout;

import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.observers.TicketSubject;
import com.comp62542.checkout.repositories.IRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TicketStatusUpdater {

    private final TicketSubject ticketSubject;
    private final IRepository<Ticket> ticketRepository;
    private static final List<String> statuses = Arrays.asList(
            AppConstants.ORDER_CREATED_STATUS,
            AppConstants.ORDER_ACCEPTED_STATUS,
            AppConstants.ORDER_PROCESSING_STATUS,
            AppConstants.ORDER_COMPLETE_STATUS);

    public TicketStatusUpdater(IRepository<Ticket> ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketSubject = TicketSubject.getInstance();
    }

    public void update(String ticketId) {
        Ticket ticket = this.ticketRepository.get(Ticket.class, ticketId);
        ticketSubject.notifyObservers(ticket);

        int index = statuses.indexOf(ticket.status);

        if (index < statuses.size() - 1) {
            ticket.status = statuses.get(index + 1);
            ticket.lastUpdated = LocalDateTime.now().toString();
            ticketRepository.add(ticket);
            ticketRepository.saveChanges();
        }
    }
}
