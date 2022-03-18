package com.comp62542.checkout;

import com.comp62542.checkout.builders.IShoppingCartBuilder;
import com.comp62542.checkout.builders.ShoppingCartBuilder;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.factories.IFactory;
import com.comp62542.checkout.factories.PaymentHandler;
import com.comp62542.checkout.factories.PaymentHandlerFactory;
import com.comp62542.checkout.repositories.*;
import com.comp62542.checkout.services.HelperService;
import com.comp62542.checkout.services.IHelperService;
import com.comp62542.checkout.services.ITicketService;
import com.comp62542.checkout.services.TicketService;
import com.comp62542.checkout.singletons.CouponsSingleton;
import com.comp62542.checkout.singletons.DatabaseSingleton;
import junit.framework.TestCase;
import org.junit.Ignore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Ignore
public class BaseTestCase extends TestCase {
    protected IShoppingCartBuilder shoppingCartBuilder;
    protected IRepository<Product> productRepository;
    protected IPaymentMethodRepository paymentMethodRepository;
    protected IHelperService helperService;
    protected IShoppingCartRepository shoppingCartRepository;
    protected IOrderRepository orderRepository;
    protected IRepository<Ticket> ticketRepository;
    protected ITicketService ticketService;
    protected IFactory<PaymentHandler, String> paymentHandlerFactory;
    protected TicketStatusUpdater ticketStatusUpdater;

    protected void loadDependencies() {
        // Recreate the database so that each test has fresh data
        DatabaseSingleton.createDocumentStore();

        // Repositories:
        this.productRepository = new ProductRepository();
        this.paymentMethodRepository = new PaymentMethodRepository();
        this.shoppingCartRepository = new ShoppingCartRepository();
        this.orderRepository = new OrderRepository();
        this.ticketRepository = new TicketRepository();

        // Services:
        this.helperService = new HelperService(this.productRepository, this.paymentMethodRepository);
        this.ticketService = new TicketService(ticketRepository);

        // Builders:
        shoppingCartBuilder = new ShoppingCartBuilder(this.helperService, this.productRepository);

        // Components
        ticketStatusUpdater = new TicketStatusUpdater(ticketRepository);
        paymentHandlerFactory = new PaymentHandlerFactory(orderRepository, paymentMethodRepository);
    }
}
