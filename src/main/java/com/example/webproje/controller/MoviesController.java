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
@RequestMapping("/movies")
public class MoviesController {

    private final MovieService movieService;
    private final UserService userService;
    private final RateService rateService;


    @GetMapping
    public String getAllMovies(Model model, @AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(value = "titleSearch", required = false) String titleSearch,
                               @RequestParam(value = "genreSearch", required = false) String genreSearch) {
        List<MovieDTO> movies = movieService.findMovies(titleSearch, genreSearch);
        List<RateDTO> rates = rateService.findAllRate();

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

        movies.forEach(movie -> {
            if (movie.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(movie.getImage());
                movie.setImageBase64(base64Image);
            }
        });


        model.addAttribute("rates", rates);
        model.addAttribute("movies", movies);

        model.addAttribute("titleSearchQuery", titleSearch);
        model.addAttribute("genreSearchQuery", genreSearch);


        return "movies";
    }


    @GetMapping("/add")
    public String showAddMovieForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        model.addAttribute("movie", new MovieDTO());
        return "add-movie";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute MovieDTO movieDTO,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            movieDTO.setImage(imageFile.getBytes());
        }
        movieService.addMovie(movieDTO);
        return "redirect:/movies";
    }

    @GetMapping("/update/{id}")
    public String getMovie(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        MovieDTO movieDTO = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı"));

        if(movieDTO.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(movieDTO.getImage());
            movieDTO.setImageBase64(base64Image);
        }

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        model.addAttribute("movie", movieDTO);
        return "update-movie";
    }

    @PostMapping("/update/{id}")
    public String updateRate(@ModelAttribute MovieDTO movieDTO,@PathVariable Long id,@RequestParam("imageFile") MultipartFile imageFile
                             ) throws IOException {

        MovieDTO updatedMovie = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Film bulunamadı"));


        if (!imageFile.isEmpty()) {
            movieDTO.setImage(imageFile.getBytes());
        }

        updatedMovie.setTitle(movieDTO.getTitle());
        updatedMovie.setReleaseDate(movieDTO.getReleaseDate());
        updatedMovie.setGenre(movieDTO.getGenre());
        updatedMovie.setDescription(movieDTO.getDescription());
        updatedMovie.setType(movieDTO.getType());


        movieService.updateMovie(updatedMovie);


        if (updatedMovie.getType() == Type.MOVIE) {
            return "redirect:/movies";
        } else {
            return "redirect:/series";
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
