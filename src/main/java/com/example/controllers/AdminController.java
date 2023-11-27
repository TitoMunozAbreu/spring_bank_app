package com.example.controllers;

import com.example.models.enums.AccountType;
import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import com.example.models.request.CustomerEditRequest;
import com.example.models.request.CustomerRequest;
import com.example.services.AccountService;
import com.example.services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private CustomerService customerService;
    private AccountService accountService;

    public AdminController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping
    public String homeAdmin(Model model){
        model.addAttribute("title", "Admin");
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("accounts", AccountType.values());
        model.addAttribute("roles", Role.values());
        model.addAttribute("customerRegister", new CustomerRequest());
        model.addAttribute("customerEdit", new CustomerEditRequest());
        return "admin";
    }

    @PostMapping("/new-customer")
    public String registerCustomer(@ModelAttribute("customerRegister") CustomerRequest request){
        CustomerEntity customer = request.toCustomerEntity();
        //crear cliente
        customerService.create(customer);
        //crear cuenta
        accountService.create(customer);

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id){
        //almacenar el customer por ID
        CustomerEntity customerFound = customerService.findById(id);

        //eliminar cuenta
        accountService.deleteAccountsByCustomer(customerFound);
        //eliminar cliente
        customerService.deleteById(id);

        return "redirect:/admin";
    }

    @PostMapping("/edit/{id}")
    public String editCustomer(@PathVariable Integer id,
                               @ModelAttribute("customerEdit") CustomerEditRequest editRequest){
        CustomerEntity customer = editRequest.toCustomerEntity();
        customerService.editById(id, customer);

        return "redirect:/admin";
    }

}
