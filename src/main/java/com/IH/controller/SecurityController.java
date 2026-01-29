package com.IH.controller;

import com.IH.service.SecurityService;
import com.IH.model.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String registration(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            @RequestParam("firstname") String firstname,
            @RequestParam("birthDate")@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate birthDate,
            Model model
            ) {
        UserResponse userResponse = securityService.registration(login, password, firstname,birthDate);
        model.addAttribute("login", userResponse.getLogin());
        model.addAttribute("firstname", userResponse.getFirstname());
        System.out.println("ok");
        return "profile";
    }
}