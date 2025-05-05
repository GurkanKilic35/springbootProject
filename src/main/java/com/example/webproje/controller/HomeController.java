package com.example.webproje.controller;

import com.example.webproje.entity.UserEntity;
import com.example.webproje.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final UserService userService;

    // Ana sayfa
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails){
        // Kullanıcı giriş yapmışsa bilgilerini model'e ekle
        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("userId", user.getId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());
        } else {
            // Kullanıcı giriş yapmamışsa varsayılan bilgileri ekle
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
        }
        return "home"; // home.html
    }

}