package com.comp62542.checkout.controllers;

import com.comp62542.checkout.mediators.IMediator;
import com.comp62542.checkout.models.OrderStatusModel;
import com.comp62542.checkout.viewModels.OrderViewModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final IMediator mediator;

    public OrderController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/order")
    public String delivery(@ModelAttribute("model") OrderViewModel model, @RequestParam("t") String ticketId) {
        model.ticketId = ticketId;
        return "order";
    }

    @PostMapping("/order")
    public String order(
            @RequestParam("type") String paymentType,
            @RequestParam("id") String paymentId) {

        String ticketId = this.mediator.handleNewOrder(paymentType, paymentId);
        return String.format("redirect:/order?t=%s", ticketId);
    }

    @ResponseBody
    @GetMapping(value = "/order/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderStatusModel orderStatus(@RequestParam("t") String ticketId, OrderStatusModel model) {
        this.mediator.query(model, ticketId);
        return model;
    }
}
