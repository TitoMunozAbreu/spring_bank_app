package com.example.controllers;

import com.example.models.AccountEntity;
import com.example.models.OperationEntity;
import com.example.models.request.OperationRequest;
import com.example.models.response.CustomerUserResponse;
import com.example.services.AccountService;
import com.example.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;
    private AccountService accountService;

    public CustomerController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping
    public String homeCustomer(Model model){
        //buscar customer segun ID
        CustomerUserResponse customer = CustomerUserResponse
                .toCustomerUserResponse(customerService.findById(1));
        //mostrar customer segun ID
        model.addAttribute("customer", customer);
        model.addAttribute("operationRequest", new OperationRequest());
        return "customer";
    }

    @PostMapping
    public String newOperation(@PathVariable Integer id,
                               @ModelAttribute("operationRequest") OperationRequest request){
        AccountEntity accountFound = accountService.findById(id);
        // to OperationEntity
        OperationEntity operation = request.toOperationEntity(accountFound);
        //añadir operation a la account
        accountService.addOperation(id,operation);

        return "redirect:/customer";
    }


}
