package com.comp62542.checkout.services;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.repositories.IRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TicketService implements ITicketService {

    private final IRepository<Ticket> ticketRepository;

    public TicketService(IRepository<Ticket> ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket createTicket(ShoppingCart shoppingCart, String paymentId, String paymentType) {
        Ticket ticket = new Ticket();
        ticket.orderId = shoppingCart.id;
        ticket.paymentId = paymentId;
        ticket.paymentType = paymentType;
        ticket.orderCreatedAt = LocalDateTime.now().toString();
        ticket.lastUpdated = LocalDateTime.now().toString();
        ticket.status = AppConstants.ORDER_CREATED_STATUS;

        ticketRepository.add(ticket);
        ticketRepository.saveChanges();

        return ticket;
    }

    @Override
    public String getTicketStatus(String ticketId) {
        Ticket ticket = this.ticketRepository.get(Ticket.class, ticketId);
        return ticket.status;
    }
}
