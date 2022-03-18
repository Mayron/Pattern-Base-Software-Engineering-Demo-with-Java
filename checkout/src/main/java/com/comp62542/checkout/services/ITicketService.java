package com.comp62542.checkout.services;

import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.Ticket;
import org.springframework.stereotype.Service;

@Service
public interface ITicketService {
    Ticket createTicket(ShoppingCart shoppingCart, String paymentId, String paymentType);
    String getTicketStatus(String ticketId);
}
