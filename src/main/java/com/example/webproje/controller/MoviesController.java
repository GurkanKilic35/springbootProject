package com.example.webproje.controller;

import com.example.webproje.dto.MovieDTO;
import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.UserEntity;
import com.example.webproje.enums.Type;
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
@RequestMapping("/movies") // Bu controller'daki sayfalar "/movies" ile başlar
public class MoviesController {

    private final MovieService movieService;
    private final UserService userService;
    private final RateService rateService;


    // Tüm filmleri (veya arama sonuçlarını) listeler
    @GetMapping
    public String getAllMovies(Model model, @AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(value = "titleSearch", required = false) String titleSearch,
                               @RequestParam(value = "genreSearch", required = false) String genreSearch) {
        // Filmleri ve puanlamaları al
        List<MovieDTO> movies = movieService.findMovies(titleSearch, genreSearch);
        List<RateDTO> rates = rateService.findAllRate();

        // Giriş yapmış kullanıcı bilgilerini ve henüz puanlamadığı filmleri model'e ekle
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

        // Film resimlerini Base64'e dönüştür
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


        return "movies"; // movies.html
    }


    // Film ekleme formunu gösterir
    @GetMapping("/add")
    public String showAddMovieForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Kullanıcı bilgilerini model'e ekle
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Boş film objesini model'e ekle
        model.addAttribute("movie", new MovieDTO());
        return "add-movie"; // add-movie.html
    }

    // Yeni film ekler
    @PostMapping("/add")
    public String addMovie(@ModelAttribute MovieDTO movieDTO,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // Resim dosyasını işleyip filme ekle
        if (!imageFile.isEmpty()) {
            movieDTO.setImage(imageFile.getBytes());
        }
        // Filmi kaydet
        movieService.addMovie(movieDTO);

        // Film türüne göre yönlendir
        if (movieDTO.getType() == Type.MOVIE) {
            return "redirect:/movies";
        } else {
            return "redirect:/series";
        }
    }

    // Belirli bir filmi güncelleme formunu gösterir
    @GetMapping("/update/{id}")
    public String getMovie(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Filmi bul
        MovieDTO movieDTO = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı"));

        // Resim varsa Base64'e dönüştür
        if(movieDTO.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(movieDTO.getImage());
            movieDTO.setImageBase64(base64Image);
        }

        // Kullanıcı bilgilerini model'e ekle
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Filmi model'e ekle
        model.addAttribute("movie", movieDTO);
        return "update-movie"; // update-movie.html
    }

    // Belirli bir filmi günceller
    @PostMapping("/update/{id}")
    public String updateRate(@ModelAttribute MovieDTO movieDTO, @PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {

        // Güncellenecek filmi bul
        MovieDTO updatedMovie = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Film bulunamadı"));

        // Yeni resim varsa güncelle
        if (!imageFile.isEmpty()) {
            updatedMovie.setImage(imageFile.getBytes()); // Dikkat: Burası movieDTO.setImage olmalı, updatedMovie'a set edilmiyor
        }

        // Film bilgilerini güncelle
        updatedMovie.setTitle(movieDTO.getTitle());
        updatedMovie.setReleaseDate(movieDTO.getReleaseDate());
        updatedMovie.setGenre(movieDTO.getGenre());
        updatedMovie.setDescription(movieDTO.getDescription());
        updatedMovie.setType(movieDTO.getType());

        // Filmi kaydet
        movieService.updateMovie(updatedMovie);

        // Film türüne göre yönlendir
        if (updatedMovie.getType() == Type.MOVIE) {
            return "redirect:/movies";
        } else {
            return "redirect:/series";
        }
    }

    // Belirli bir filmi siler
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        // Filmi sil
        movieService.deleteMovie(id);
        return "redirect:/movies"; // Filmler sayfasına yönlendir
    }
}