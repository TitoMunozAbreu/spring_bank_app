package com.example.controllers;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.models.OperationEntity;
import com.example.models.enums.Transaction;
import com.example.models.request.OperationRequest;
import com.example.models.response.AccountsResponse;
import com.example.models.response.CustomerUserResponse;
import com.example.services.AccountService;
import com.example.services.CustomerService;
import com.example.services.OperationService;
import jakarta.validation.Valid;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;
    private AccountService accountService;
    private OperationService operationService;

    public CustomerController(CustomerService customerService, AccountService accountService, OperationService operationService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @GetMapping
    public String homeCustomer(Model model, Authentication authentication){
        CustomerEntity customerAuthenticated = (CustomerEntity) this.customerService.loadUserByUsername(authentication.getName());
        //buscar customer segun ID
        CustomerUserResponse customer = CustomerUserResponse
                .toCustomerUserResponse(customerAuthenticated);
        //almacenar todas las accounts sin el customer en sesion
        List<AccountsResponse> ibansDestination = accountService.findAllNotIncludeCustomerInSession(customer.accounts());
        //mostrar customer segun ID
        model.addAttribute("customer", customer);
        model.addAttribute("operationRequest", new OperationRequest());
        model.addAttribute("transactions", Transaction.values());
        model.addAttribute("ibansDestination", ibansDestination);
        return "customer";
    }

    @PostMapping("/{id}/new-operation")
    public String newOperation(Model model,
                               Authentication authentication,
                               @PathVariable Integer id,
                               @Valid @ModelAttribute("operationRequest") OperationRequest request,
                               BindingResult result,
                               RedirectAttributes attributes){
        //comprobar validaciones
        if(result.hasErrors()){
            model.addAttribute("warning", " New Operation data invalid!");

            CustomerEntity customerAuthenticated = (CustomerEntity) this.customerService.loadUserByUsername(authentication.getName());
            //buscar customer segun ID
            CustomerUserResponse customer = CustomerUserResponse
                    .toCustomerUserResponse(customerAuthenticated);
            //almacenar todas las accounts sin el customer en sesion
            List<AccountsResponse> ibansDestination = accountService.findAllNotIncludeCustomerInSession(customer.accounts());
            //mostrar customer segun ID
            model.addAttribute("customer", customer);
            model.addAttribute("transactions", Transaction.values());
            model.addAttribute("ibansDestination", ibansDestination);
            return "customer";
        }

        //customer segun ibanSource
        AccountEntity customerSource = accountService.findByIban(request.getIbanSource());
        //comprobar si el withdraw es menor o igual a balance
        if(request.getTransaction().equals(Transaction.WITHDRAW) && customerSource.getBalance() < request.getAmount()){
            attributes.addFlashAttribute("danger", "Failed Withdraw: Balance is below the amount request");
            return "redirect:/customer";
        }
        if(request.getTransaction().equals(Transaction.TRANSFER) && customerSource.getBalance() < request.getAmount()){
            attributes.addFlashAttribute("danger", "Failed Transfer: Balance is below the amount request");
            return "redirect:/customer";
        }
        //toOperationEntity
        OperationEntity operationSource = request.toOperationEntity(customerSource);
        //create operation
        operationService.create(operationSource);

        //comprobar el tipo de transaction
        if(request.getTransaction().equals(Transaction.TRANSFER) && customerSource.getBalance() >= request.getAmount()){
            //customer segun ibanDestination
            AccountEntity customerDestination = accountService.findByIban(request.getIbanDestination());
            //toOperationEntity
            OperationEntity operationDestination = request.toOperationEntity(customerDestination);
            //create operation
            operationService.create(operationDestination);
        }
        //registrar operation en account
        accountService.registerOperation(request);
        return "redirect:/customer";
    }


}
