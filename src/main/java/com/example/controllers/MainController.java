package com.example.controllers;

import com.example.models.request.LoginResquest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class MainController {

    @GetMapping()
    public String home(Model model){
        model.addAttribute("loginRequest", new LoginResquest());
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "home";
    }

}
