package com.example.webproje.repository;

import com.example.webproje.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<MovieEntity,Long> {
}
