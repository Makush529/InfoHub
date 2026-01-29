package com.IH.controller;

import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.service.SecurityService;
import com.IH.model.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;

@Controller
@EnableWebMvc
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute @Valid
                               RequestRegistrationDTO requestRegistrationDTO,
                               BindingResult bindingResult,
                               Model model){
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                System.out.println(error.getDefaultMessage());
            }
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "error";
        }

        UserResponse userResponse = securityService.registration(requestRegistrationDTO);
        model.addAttribute("login", userResponse.getLogin());
        model.addAttribute("firstname", userResponse.getFirstname());
        System.out.println("ok");
        return "profile";
    }
}