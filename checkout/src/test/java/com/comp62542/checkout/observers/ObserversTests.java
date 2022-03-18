package com.comp62542.checkout.observers;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.BaseTestCase;
import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.factories.PaymentHandlerFactory;
import org.junit.jupiter.api.DisplayName;
import org.mockito.internal.matchers.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObserversTests extends BaseTestCase {

    private PaymentHandlerFactory paymentHandlerFactory;

    public void setUp() {
        loadDependencies();
        this.paymentHandlerFactory = new PaymentHandlerFactory(orderRepository, paymentMethodRepository);
    }

    @DisplayName("Test when notifying all observers with status, only the one observer depending on " +
            "that status should fire its update method and then unregister itself from the subject")
    public void testWhenCallingNotifyAllOnSubjectWithStatusShouldOnlyTriggerAndUnregisterCorrectObserver() {

        // TicketSubject is a singleton
        ISubject<Ticket> ticketSubject = TicketSubject.getInstance();

        // register observers
        IObserver<Ticket> createdObserver = new OrderCreatedObserver(this.orderRepository, this.shoppingCartRepository);
        IObserver<Ticket> acceptedObserver = new OrderAcceptedObserver(this.shoppingCartRepository);
        IObserver<Ticket> processingObserver = new OrderProcessingObserver(this.orderRepository, this.paymentHandlerFactory);
        IObserver<Ticket> completeObserver = new OrderCompleteObserver(this.orderRepository);

        ticketSubject.register(createdObserver);
        ticketSubject.register(acceptedObserver);
        ticketSubject.register(processingObserver);
        ticketSubject.register(completeObserver);

        // Create required order fetched by the observers
        Order order = new Order();
        order.orderId = "test order";
        order.completed = null; // this will be updated after executing the observer

        orderRepository.add(order);
        orderRepository.saveChanges();

        // Create state
        Ticket ticket = new Ticket();
        ticket.status = AppConstants.ORDER_COMPLETE_STATUS;
        ticket.orderId = order.orderId;

        // Act - notify the observers of the new state
        ticketSubject.notifyObservers(ticket);

        // Assert
        assertNull("observer should have triggered update and unregistered itself",
                completeObserver.getSubject());

        // These three should not have been triggered and are still observing subject
        assertEquals(ticketSubject, createdObserver.getSubject());
        assertEquals(ticketSubject, acceptedObserver.getSubject());
        assertEquals(ticketSubject, processingObserver.getSubject());

        orderRepository.refresh(order); // refresh the object stored in RavenDB to reflect new state
        assertTrue(order.completed.length() > 0);
    }
}
