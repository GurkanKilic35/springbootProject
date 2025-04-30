package com.example.webproje.dto;

import com.example.webproje.entity.MovieEntity;
import com.example.webproje.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RateDTO {
    private long id;
    private UserEntity user;
    private MovieEntity movie;
    private int score;
    private LocalDateTime ratingDate;
    private String comment;
}
