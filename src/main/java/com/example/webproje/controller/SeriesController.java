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

import java.util.Base64;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/series") // Bu controller'daki tüm sayfalar "/series" ile başlar
public class SeriesController {

    private final MovieService movieService;
    private final UserService userService;
    private final RateService rateService;


    // Tüm dizileri (veya arama sonuçlarını) listeler
    @GetMapping
    public String getAllSeries(Model model, @AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(value = "titleSearch", required = false) String titleSearch,
                               @RequestParam(value = "genreSearch", required = false) String genreSearch) {
        // Filmleri (dizileri) ve puanlamaları al
        List<MovieDTO> movies = movieService.findMovies(titleSearch, genreSearch);
        List<RateDTO> rates = rateService.findAllRate();

        // Kullanıcı giriş yapmışsa bilgilerini ve henüz puanlamadığı dizileri model'e ekle
        Long userId = null;
        if (userDetails != null) {
            UserEntity user = userService.findByUsername(userDetails.getUsername());
            userId = user.getId();
            model.addAttribute("userId", user.getId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userRole", user.getRole().name());

            List<MovieDTO> moviesNotRatedByUser = movies;
            if (userId != null) {
                moviesNotRatedByUser = movies.stream()
                        .filter(movie -> rates.stream()
                                .noneMatch(rate -> rate.getMovie().getId()==movie.getId()
                                        && rate.getUser().getId()==user.getId()))
                        .toList();
            }
            model.addAttribute("moviesNotRatedByUser", moviesNotRatedByUser);

        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("userRole", "GUEST");
        }

        // Dizi resimlerini Base64'e dönüştür
        movies.forEach(movie -> {
            if (movie.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(movie.getImage());
                movie.setImageBase64(base64Image);
            }
        });

        // Modelleri view'a ekle
        model.addAttribute("rates", rates);
        model.addAttribute("movies", movies);

        model.addAttribute("titleSearchQuery", titleSearch);
        model.addAttribute("genreSearchQuery", genreSearch);

        return "series"; // series.html
    }

}