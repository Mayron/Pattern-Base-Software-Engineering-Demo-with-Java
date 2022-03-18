package com.comp62542.checkout.mediators;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.builders.IShoppingCartBuilder;
import com.comp62542.checkout.TicketStatusUpdater;
import com.comp62542.checkout.commands.*;
import com.comp62542.checkout.domain.*;
import com.comp62542.checkout.factories.IFactory;
import com.comp62542.checkout.factories.PaymentHandler;
import com.comp62542.checkout.models.OrderStatusModel;
import com.comp62542.checkout.observers.*;
import com.comp62542.checkout.repositories.IOrderRepository;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.repositories.IShoppingCartRepository;
import com.comp62542.checkout.services.IHelperService;
import com.comp62542.checkout.services.ITicketService;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import com.comp62542.checkout.viewModels.IndexViewModel;
import com.comp62542.checkout.viewModels.PaymentViewModel;
import com.comp62542.checkout.models.FiltersModel;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Controls services and executes commands based off of query logic
@Service
public class Mediator implements IMediator {

    // Repositories
    private final IRepository<Product> productRepository;
    private final IShoppingCartRepository shoppingCartRepository;
    private final IPaymentMethodRepository paymentMethodRepository;
    private final IOrderRepository orderRepository;
    private final IRepository<Ticket> ticketRepository;

    // Services
    private final IHelperService helperService;
    private final ITicketService ticketService;

    // Builders
    private final IShoppingCartBuilder shoppingCartBuilder;

    // Components
    private final TicketStatusUpdater ticketStatusUpdater;
    private final IFactory<PaymentHandler, String> paymentHandlerFactory;

    public Mediator(
            IShoppingCartBuilder shoppingCartBuilder,
            IRepository<Product> productRepository,
            IShoppingCartRepository shoppingCartRepository,
            IPaymentMethodRepository paymentMethodRepository,
            IHelperService helperService,
            ITicketService ticketService,
            IOrderRepository orderRepository,
            IRepository<Ticket> ticketRepository,
            TicketStatusUpdater ticketStatusUpdater,
            IFactory<PaymentHandler, String> paymentHandlerFactory)
    {
        this.shoppingCartBuilder = shoppingCartBuilder;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.helperService = helperService;
        this.ticketService = ticketService;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.ticketStatusUpdater = ticketStatusUpdater;
        this.paymentHandlerFactory = paymentHandlerFactory;
    }

    @Override
    public void query(IndexViewModel model, HttpSession session) {
        List<Product> products = this.productRepository.getAll(Product.class);
        model.products = new ArrayList<>(products.size());

        for (Product product : products) {
            IndexViewModel.Product productModel = this.helperService.map(product);
            model.products.add(productModel);
        }

        if (session.getAttribute("filters") != null) {
            model.filters = (FiltersModel) session.getAttribute("filters");
            this.helperService.applyFilters(model.filters, model.products);
        }
        else {
            model.filters = new FiltersModel();
        }

        if (session.getAttribute("query") != null) {
            model.query = (String) session.getAttribute("query");
            this.helperService.applySearchQuery(model.products, model.query);
            session.removeAttribute("query");
        }
    }

    @Override
    public void query(CheckoutViewModel model) {
        model.items = new ArrayList<>();

        ShoppingCart shoppingCart = this.shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        for (ShoppingCartItem item : shoppingCart.items) {
            Product product = this.productRepository.get(Product.class, item.productId);
            CheckoutViewModel.Item itemModel = this.helperService.map(item, product);
            model.items.add(itemModel);
        }

        this.helperService.map(model, shoppingCart);
    }

    @Override
    public void query(PaymentViewModel model, boolean addCard) {
        List<PaymentMethod> methods = this.paymentMethodRepository.getCards();
        model.addCard = addCard;
        model.cards = new ArrayList<>();

        for (PaymentMethod method : methods) {
            PaymentViewModel.Card card = this.helperService.map(method);
            model.cards.add(card);
        }

        PaymentMethod payPal = this.paymentMethodRepository
                .getAlternativePaymentMethod(AppConstants.PAYPAL_PAYMENT_TYPE);

        PaymentMethod gPay = this.paymentMethodRepository
                .getAlternativePaymentMethod(AppConstants.GOOGLE_PAY_PAYMENT_TYPE);

        model.paypalId = this.paymentMethodRepository.getDocumentId(payPal);
        model.googlePayId = this.paymentMethodRepository.getDocumentId(gPay);
    }

    @Override
    public void query(OrderStatusModel model, String ticketId) {
        model.status = this.ticketService.getTicketStatus(ticketId);
        this.ticketStatusUpdater.update(ticketId);
    }

    @Override
    public void handleCheckout(Map<String, String[]> inputs) {
        ShoppingCart shoppingCart = getShoppingCart();
        List<ShoppingCartItem> items = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : inputs.entrySet()) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.productId = entry.getKey();
            item.quantity = Integer.parseInt(entry.getValue()[0]);

            items.add(item);
        }

        executeRebuildShoppingCartCommand(items, shoppingCart.couponApplied);
    }

    @Override
    public void handleCheckoutRemove(String productId) {
        ShoppingCart shoppingCart = getShoppingCart();
        shoppingCart.items.removeIf(i -> i.productId.equals(productId));
        executeRebuildShoppingCartCommand(shoppingCart.items, shoppingCart.couponApplied);
    }

    @Override
    public void handleCheckoutAddCoupon(String couponCode) {
        ShoppingCart shoppingCart = getShoppingCart();
        executeRebuildShoppingCartCommand(shoppingCart.items, couponCode);
    }

    @Override
    public void handleCheckoutRemoveCoupon() {
        ShoppingCart shoppingCart = getShoppingCart();
        executeRebuildShoppingCartCommand(shoppingCart.items, null);
    }

    @Override
    public void handlePaymentAddCard(String name, String number, String expiry) {
        ICommand command = new AddPaymentMethodCommand(this.paymentMethodRepository, name, number, expiry);
        command.execute();
    }

    @Override
    public void handlePaymentRemoveCard(String cardId) {
        ICommand command = new RemovePaymentMethodCommand(this.paymentMethodRepository, cardId);
        command.execute();
    }

    @Override
    public String handleNewOrder(String paymentType, String paymentId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        if (shoppingCart == null)
            return null;

        Ticket ticket = this.ticketService.createTicket(shoppingCart, paymentId, paymentType);

        ISubject<Ticket> orderSubject = TicketSubject.getInstance();
        orderSubject.register(new OrderCreatedObserver(this.orderRepository, this.shoppingCartRepository));
        orderSubject.register(new OrderAcceptedObserver(this.shoppingCartRepository));
        orderSubject.register(new OrderProcessingObserver(this.orderRepository, this.paymentHandlerFactory));
        orderSubject.register(new OrderCompleteObserver(this.orderRepository));

        return this.ticketRepository.getDocumentId(ticket);
    }

    @Override
    public void handleIndexFilter(HttpSession session, String milk, String bread, String fruit) {
        FiltersModel filters = new FiltersModel();
        filters.milk = milk != null;
        filters.bread = bread != null;
        filters.fruit = fruit != null;

        session.setAttribute("filters", filters);
    }

    @Override
    public void handleIndexSearch(HttpSession session, String query) {
        session.removeAttribute("filters");
        session.setAttribute("query", query);
    }

    private ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = this.shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);
        return shoppingCart != null ? shoppingCart : new ShoppingCart();
    }

    private void executeRebuildShoppingCartCommand(List<ShoppingCartItem> items, String couponCode) {
        ICommand command = new RebuildShoppingCartCommand(
                shoppingCartBuilder,
                shoppingCartRepository,
                productRepository,
                items,
                couponCode);

        command.execute();
    }
}