package com.example.webproje.controller;

import com.example.webproje.dto.MovieDTO;
import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.UserEntity;
import com.example.webproje.service.MovieService;
import com.example.webproje.service.RateService;
import com.example.webproje.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/series")
public class SeriesController {

    private final MovieService movieService;
    private final UserService userService;
    private final RateService rateService;


    @GetMapping
    public String getAllSeries(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<MovieDTO> movies = movieService.findAllMovie();
        List<RateDTO> rates = rateService.findAllRate(); // Fetch all rates

        Long userId = null;
        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            userId = user.getId();
            model.addAttribute("userId", user.getId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());

            // This list is not strictly necessary for the template logic requested,
            // but we'll keep it as it was in your original code.
            List<MovieDTO> moviesNotRatedByUser = movies;
            if (userId != null) {
                moviesNotRatedByUser = movies.stream()
                        .filter(movie -> rates.stream()
                                .noneMatch(rate -> rate.getMovie().getId()==movie.getId()// Use .equals for Long comparison
                                        && rate.getUser().getId()==user.getId())) // Use .equals for Long comparison
                        .toList(); // Java 16+, use collect(Collectors.toList()) for Java 8
            }
            model.addAttribute("moviesNotRatedByUser", moviesNotRatedByUser);

        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
            // If guest, no userId is set, which is handled in template
        }

        movies.forEach(movie -> {
            if (movie.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(movie.getImage());
                movie.setImageBase64(base64Image);
            }
        });

        // Pass all rates to the template
        model.addAttribute("rates", rates);
        model.addAttribute("movies", movies);


        return "series";
    }

}
