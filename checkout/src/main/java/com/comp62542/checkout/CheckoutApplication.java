package com.comp62542.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CheckoutApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CheckoutApplication.class, args);
    }
}