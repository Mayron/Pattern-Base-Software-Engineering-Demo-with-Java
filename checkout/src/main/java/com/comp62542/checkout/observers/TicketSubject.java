package com.comp62542.checkout.observers;

import com.comp62542.checkout.domain.Ticket;

public class TicketSubject extends BaseSubject<Ticket> {
    private static TicketSubject instance;

    private TicketSubject(){
        super();
    }

    public static TicketSubject getInstance() {
        if (instance == null) {
            instance = new TicketSubject();
        }

        return instance;
    }
}
