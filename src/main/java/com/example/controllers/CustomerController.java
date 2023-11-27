package com.example.controllers;

import com.example.models.response.CustomerUserResponse;
import com.example.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerRepository) {
        this.customerService = customerRepository;
    }

    @GetMapping
    public String homeCustomer(Model model){
        //buscar customer segun ID
        CustomerUserResponse customer = CustomerUserResponse
                .toCustomerUserResponse(customerService.findById(1));
        //mostrar customer segun ID
        model.addAttribute("customer", customer);
        return "customer";
    }


}
