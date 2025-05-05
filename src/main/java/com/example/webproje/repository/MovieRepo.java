package com.example.webproje.repository;

import com.example.webproje.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepo extends JpaRepository<MovieEntity,Long> {
    List<MovieEntity> findByTitleContainingIgnoreCase(String title);
    List<MovieEntity> findByGenreContainingIgnoreCase(String genre);
    List<MovieEntity> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(String title, String genre);
}
