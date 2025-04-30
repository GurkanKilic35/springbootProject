package com.example.webproje.controller;

import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.MovieEntity;
import com.example.webproje.entity.RateEntity;
import com.example.webproje.entity.UserEntity;
import com.example.webproje.enums.Type;
import com.example.webproje.mapper.MovieMapper;
import com.example.webproje.service.MovieService;
import com.example.webproje.service.RateService;
import com.example.webproje.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.example.webproje.dto.MovieDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@Controller
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    MovieService movieService;
    RateService rateService;
    UserService userService;

    @GetMapping("/rate/{id}")
    public String getMovie(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        MovieDTO movie = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı"));

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("username", user.getUsername());

        RateDTO rateDTO = new RateDTO();
        model.addAttribute("movie", movie);
        model.addAttribute("rate",rateDTO);
        return "movie";
    }

    @PostMapping("/rate/{id}")
    public String rateMovie(@ModelAttribute RateDTO rate,
                            @AuthenticationPrincipal User userDetails,
                            @PathVariable Long id) {

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        MovieEntity movie = MovieMapper.dtoToEntity(movieService.findMovieById(id).get());


        rate.setMovie(movie);
        rate.setUser(user);

        // Puanı ekle
        rateService.addRate(rate);

        if(movie.getType() == Type.MOVIE){
            return "redirect:/movies";
        }else{
            return "redirect:/series";
        }
    }

    @GetMapping("/rate/update/{id}")
    public String getRate(@PathVariable Long id, Model model) {
        RateDTO rateDTO = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Puan bulunamadı"));
        model.addAttribute("rate", rateDTO);
        model.addAttribute("movie", rateDTO.getMovie());
        return "update-rate";
    }

    @PostMapping("/rate/update/{id}")
    public String updateRate(@ModelAttribute RateDTO rate,
                             @AuthenticationPrincipal User userDetails,
                             @PathVariable Long id) {

        RateDTO updatedRate = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek puan bulunamadı"));

        updatedRate.setScore(rate.getScore());
        updatedRate.setComment(rate.getComment());

        rateService.updateRate(updatedRate);

        if (updatedRate.getMovie().getType() == Type.MOVIE) {
            return "redirect:/movies";
        } else {
            return "redirect:/series";
        }
    }

    @GetMapping("/rate/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {

        RateDTO deletedRate = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek puan bulunamadı"));
        long movieId = deletedRate.getMovie().getId();

        rateService.deleteRate(id);


        MovieEntity movie = MovieMapper.dtoToEntity(movieService.findMovieById(movieId).get());
        if(movie.getType() == Type.MOVIE){
            return "redirect:/movies";
        }else{
            return "redirect:/series";
        }
    }

}
