package com.example.webproje.controller;

import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.MovieEntity;
import com.example.webproje.entity.UserEntity;
import com.example.webproje.enums.Type;
import com.example.webproje.mapper.MovieMapper;
import com.example.webproje.service.MovieService;
import com.example.webproje.service.RateService;
import com.example.webproje.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.example.webproje.dto.MovieDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@Controller
@AllArgsConstructor
@RequestMapping("/movie") // Bu controller'daki tüm sayfalar "/movie" ile başlar
public class MovieController {

    MovieService movieService;
    RateService rateService;
    UserService userService;

    // Belirli bir film için puanlama sayfasını gösterir
    @GetMapping("/rate/{id}")
    public String getMovie(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Filmi bul
        MovieDTO movie = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı"));

        // Giriş yapmış kullanıcıyı bul ve model'e ekle
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Model'e film ve yeni puanlama objesi ekle
        RateDTO rateDTO = new RateDTO();
        model.addAttribute("movie", movie);
        model.addAttribute("rate",rateDTO);
        return "movie"; // movie.html view'ını döndür
    }

    // Belirli bir filme puan verir
    @PostMapping("/rate/{id}")
    public String rateMovie(@ModelAttribute RateDTO rate, // Formdan gelen puanlama verileri
                            @AuthenticationPrincipal UserDetails userDetails, // Giriş yapmış kullanıcı
                            @PathVariable Long id) { // Film ID'si

        // Kullanıcı ve filmi bul, puanlama objesine set et
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        MovieEntity movie = MovieMapper.dtoToEntity(movieService.findMovieById(id).get());
        rate.setMovie(movie);
        rate.setUser(user);

        // Puanlamayı kaydet
        rateService.addRate(rate);

        // Film türüne göre yönlendir
        if(movie.getType() == Type.MOVIE){
            return "redirect:/movies";
        }else{
            return "redirect:/series";
        }
    }

    // Mevcut bir puanlamayı güncelleme sayfasını gösterir
    @GetMapping("/rate/update/{id}") //
    public String getRate(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Puanlamayı bul
        RateDTO rateDTO = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Puan bulunamadı"));

        // Giriş yapmış kullanıcıyı bul ve model'e ekle
        UserEntity user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());

        // Model'e puanlama ve film objesi ekle
        model.addAttribute("rate", rateDTO);
        model.addAttribute("movie", rateDTO.getMovie());
        return "update-rate";
    }

    // Mevcut bir puanlamayı günceller
    @PostMapping("/rate/update/{id}")
    public String updateRate(@ModelAttribute RateDTO rate, // Formdan gelen güncel puanlama verileri
                             @AuthenticationPrincipal UserDetails userDetails, // Giriş yapmış kullanıcı
                             @PathVariable Long id) { // Güncellenecek puanlama ID'si

        // Güncellenecek puanlamayı bul
        RateDTO updatedRate = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek puan bulunamadı"));

        // Puanlama skorunu ve yorumunu güncelle
        updatedRate.setScore(rate.getScore());
        updatedRate.setComment(rate.getComment());

        // Puanlamayı kaydet
        rateService.updateRate(updatedRate);

        // Film türüne göre yönlendir
        if (updatedRate.getMovie().getType() == Type.MOVIE) {
            return "redirect:/movies";
        } else {
            return "redirect:/series";
        }
    }

    // Bir puanlamayı siler
    @GetMapping("/rate/delete/{id}")
    public String deleteMovie(@PathVariable Long id) { // Silinecek puanlama ID'si

        // Silinecek puanlamayı bul
        RateDTO deletedRate = rateService.findRateById(id)
                .orElseThrow(() -> new RuntimeException("Silinecek puan bulunamadı"));
        // Puanlamanın ait olduğu filmin ID'sini al
        long movieId = deletedRate.getMovie().getId();

        // Puanlamayı sil
        rateService.deleteRate(id);

        // Film türüne göre yönlendir
        MovieEntity movie = MovieMapper.dtoToEntity(movieService.findMovieById(movieId).get());
        if(movie.getType() == Type.MOVIE){
            return "redirect:/movies";
        }else{
            return "redirect:/series";
        }
    }

}