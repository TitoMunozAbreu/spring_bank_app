package com.example.controllers;

import com.example.models.AccountEntity;
import com.example.models.enums.AccountType;
import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import com.example.models.request.AccountRequest;
import com.example.models.request.CustomerEditRequest;
import com.example.models.request.CustomerRequest;
import com.example.services.AccountService;
import com.example.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

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
        model.addAttribute("accountRegister", new AccountRequest());
        return "admin";
    }

    @PostMapping("/new-customer")
    public String registerCustomer(Model model,
                                    @Valid @ModelAttribute("customerRegister")
                                    CustomerRequest request,
                                    BindingResult result,
                                    RedirectAttributes attributes){

        if(result.hasErrors()){
            model.addAttribute("warning", " New customer data invalid!");
            model.addAttribute("title", "Admin");
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("accounts", AccountType.values());
            model.addAttribute("roles", Role.values());
            model.addAttribute("customerEdit", new CustomerEditRequest());
            model.addAttribute("accountRegister", new AccountRequest());
            return "admin";
        }
        //comprobar si existe el usuario
        boolean userExist = customerService.findByEmail(request.getEmail()).isPresent();

        if(userExist){
            attributes.addFlashAttribute("danger", "Email already exists!");
            return "redirect:/admin";
        }

        CustomerEntity customer = request.toCustomerEntity();
        //crear cliente
        customerService.create(customer);
        //crear cuenta
        accountService.create(customer);
        attributes.addFlashAttribute("success", "Customer succesfully created!");
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
    public String editCustomer(Model model,
                                @PathVariable Integer id,
                                @Valid @ModelAttribute("customerEdit")
                                CustomerEditRequest editRequest,
                                BindingResult result){

        if(result.hasErrors()){
            model.addAttribute("warning", " Edit customer data invalid!");
            model.addAttribute("title", "Admin");
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("accounts", AccountType.values());
            model.addAttribute("customerRegister", new CustomerRequest());
            model.addAttribute("accountRegister", new AccountRequest());

            return "admin";
        }
        CustomerEntity customer = editRequest.toCustomerEntity();
        customerService.editById(id, customer);

        return "redirect:/admin";
    }

    @PostMapping("/{id}/new-account")
    public String newAccountByCustomerId(Model model,
                                         @PathVariable Integer id,
                                         @Valid @ModelAttribute("accountRegister")
                                         AccountRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("warning", " New account data invalid!");
            model.addAttribute("title", "Admin");
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("accounts", AccountType.values());
            model.addAttribute("roles", Role.values());
            model.addAttribute("customerRegister", new CustomerRequest());
            model.addAttribute("customerEdit", new CustomerEditRequest());

            return "admin";
        }

        //buscar customer segun ID
        CustomerEntity customerFound = customerService.findById(id);
        //pasar toAccountEntity
        AccountEntity account = request.toAccountEntity(customerFound);
        //guardar nueva cuenta
        accountService.create(account);

        return "redirect:/admin";

    }

}
