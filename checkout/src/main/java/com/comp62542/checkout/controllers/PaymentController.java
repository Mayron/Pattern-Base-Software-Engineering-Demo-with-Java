package com.comp62542.checkout.controllers;

import com.comp62542.checkout.mediators.IMediator;
import com.comp62542.checkout.viewModels.PaymentViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {
//    IServiceFactory<IPaymentService> paymentServiceFactory
    public IMediator mediator;

    public PaymentController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/payment")
    public String payment(@RequestParam(value = "add", required = false) String add,
                          @ModelAttribute("model") PaymentViewModel model) {

        mediator.query(model, add != null);
        return "payment";
    }

    @PostMapping("/payment/addCard")
    public String addCard(@RequestParam("name") String name,
                          @RequestParam("num") String number,
                          @RequestParam("exp") String expiry) {

        mediator.handlePaymentAddCard(name, number, expiry);
        return "redirect:/payment";
    }

    @PostMapping("/payment/removeCard")
    public String removeCard(@RequestParam("id") String cardId) {
        mediator.handlePaymentRemoveCard(cardId);
        return "redirect:/payment";
    }
}
