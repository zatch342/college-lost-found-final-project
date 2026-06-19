package com.example.lostfound.controller;

import com.example.lostfound.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/items";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam @NotBlank String name,
                           @RequestParam @Email String email,
                           @RequestParam @Size(min = 6) String password,
                           Model model) {
        try {
            userService.register(name, email, password);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            return "register";
        }
    }
}
