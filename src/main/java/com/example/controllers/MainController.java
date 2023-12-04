package com.example.controllers;

import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import com.example.models.request.LoginResquest;
import com.example.models.request.RegisterRequest;
import com.example.services.AccountService;
import com.example.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
    private CustomerService customerService;
    private AccountService accountService;

    public MainController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home(Model model){
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginRequest", new LoginResquest());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("roles", Role.values());
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(Model model,
                                   @Valid @ModelAttribute("registerRequest")
                                    RegisterRequest request,
                                    BindingResult result,
                                    RedirectAttributes attributes){
        //comprobar errores de validaciones
        if(result.hasErrors()){
            model.addAttribute("roles", Role.values());
            return "register";
        }

        //comprobar si existe el usuario
        boolean userExist = customerService.findByEmail(request.getEmail()).isPresent();

        if(userExist){
            attributes.addFlashAttribute("danger", "Email already exists!");
            return "redirect:/register";
        }

        CustomerEntity customer = request.toCustomerEntity();
        //crear cliente
        customerService.create(customer);

        attributes.addFlashAttribute("success", "user succesfully created!");
        return "redirect:/login";
    }

}
