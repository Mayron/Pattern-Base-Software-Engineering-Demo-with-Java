package com.comp62542.checkout.controllers;

import com.comp62542.checkout.mediators.IMediator;
import com.comp62542.checkout.viewModels.IndexViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private final IMediator mediator;

    public IndexController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("model") IndexViewModel model, HttpServletRequest request) {
        mediator.query(model, request.getSession());
        return "index";
    }

    @PostMapping("/filter")
    public String filter(
            @RequestParam(value = "milk", required = false) String milk,
            @RequestParam(value = "bread", required = false) String bread,
            @RequestParam(value = "fruit", required = false) String fruit,
            HttpServletRequest request) {

        mediator.handleIndexFilter(request.getSession(), milk, bread, fruit);

        return "redirect:/";
    }

    @PostMapping("/search")
    public String search(
            @RequestParam(value = "query", required = false) String query,
            HttpServletRequest request) {

        mediator.handleIndexSearch(request.getSession(), query);
        return "redirect:/";
    }
}
