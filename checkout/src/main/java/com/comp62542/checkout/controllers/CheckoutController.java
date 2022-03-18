package com.comp62542.checkout.controllers;

import com.comp62542.checkout.mediators.IMediator;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {

    private final IMediator mediator;

    public CheckoutController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/checkout")
    public String postCheckout(HttpServletRequest request) {
        mediator.handleCheckout(request.getParameterMap());
        return "redirect:checkout";
    }

    @GetMapping("/checkout")
    public String getCheckout(@ModelAttribute("model") CheckoutViewModel model) {
        mediator.query(model);
        return "checkout";
    }

    @GetMapping("/checkout/remove")
    public String remove(@RequestParam("id") String productId) {
        mediator.handleCheckoutRemove(productId);
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/addCoupon")
    public String addCoupon(@RequestParam("c") String couponCode) {
        mediator.handleCheckoutAddCoupon(couponCode);
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/removeCoupon")
    public String removeCoupon() {
        mediator.handleCheckoutRemoveCoupon();
        return "redirect:/checkout";
    }
}
