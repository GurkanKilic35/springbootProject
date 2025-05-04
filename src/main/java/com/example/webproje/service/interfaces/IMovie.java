package com.example.webproje.service.interfaces;

import com.example.webproje.dto.MovieDTO;

import java.util.List;
import java.util.Optional;

public interface IMovie {
    MovieDTO addMovie(MovieDTO movieDTO);
    List<MovieDTO> findAllMovie();
    Optional<MovieDTO> findMovieById(Long id);
    MovieDTO updateMovie(MovieDTO movieDTO);
    List<MovieDTO> findMovies(String title, String genre);
    void deleteMovie(Long id);
}
