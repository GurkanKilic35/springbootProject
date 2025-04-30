package com.example.webproje.controller;

import org.springframework.ui.Model;
import com.example.webproje.dto.UserDTO;
import com.example.webproje.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(@AuthenticationPrincipal UserDTO user) {
        if (user != null) {
            return "redirect:/home";
        }
        return "login";
    }
}
