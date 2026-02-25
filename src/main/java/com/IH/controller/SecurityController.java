package com.IH.controller;

import com.IH.model.dto.RequestLoginDTO;
import com.IH.model.dto.RequestRegistrationDTO;
import com.IH.service.SecurityService;
import com.IH.model.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
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
                               HttpSession session,
                               Model model) throws SQLException {
        if (bindingResult.hasErrors()) {
            System.out.println("=== ОБНАРУЖЕНЫ ОШИБКИ ВАЛИДАЦИИ ===");

            for (ObjectError error : bindingResult.getAllErrors()) {
                // Выводим тип ошибки и сообщение
                System.out.println("Ошибка: " + error.getObjectName() + " - " + error.getDefaultMessage());
                // Если это ошибка конкретного поля (FieldError), выведем и имя поля
                if (error instanceof FieldError fieldError) {
                    System.out.println("Поле: " + fieldError.getField() + " | Значение: " + fieldError.getRejectedValue());
                }
            }

            model.addAttribute("errors", bindingResult.getAllErrors());
            return "error";
        }

        UserResponse userResponse = securityService.registration(requestRegistrationDTO);
        model.addAttribute("login", userResponse.getLogin());
        model.addAttribute("username", userResponse.getUsername());
        session.setAttribute("username", userResponse.getUsername());
        session.setAttribute("userId", userResponse.getId());
        System.out.println("ok");
        return "redirect:/security/profile";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login"; // имя jsp файла
    }

    @PostMapping("/login")
    public String login(@ModelAttribute RequestLoginDTO loginDTO,
                        HttpSession session, Model model) throws SQLException {
        UserResponse user = securityService.login(loginDTO); // Измени тип возвращаемого значения в сервисе
        if (user != null) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());
            return "redirect:/security/profile"; // идем в профиль, редирект чтобы не переотправлять форму при Ф5
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login"; // ошибка
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");
        if (username == null) {
            return "redirect:/security/login";
        }
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/security/login";
    }
}