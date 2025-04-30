package com.example.webproje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movie_series_id", nullable = false)
    private MovieEntity movie;

    @Min(value = 1, message = "Puan 1'den küçük olamaz.")
    @Max(value = 10, message = "Puan 10'dan büyük olamaz.")
    private int score;
    private LocalDateTime ratingDate=LocalDateTime.now();

    @Size(max = 500, message = "Yorum en fazla 500 karakter olabilir.")
    @Column(length = 500)
    private String comment;

    @Version
    private Long version= 0L;;

    public RateEntity(long id, UserEntity user, MovieEntity movie, int score, LocalDateTime ratingDate, String comment) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.score = score;
        this.ratingDate = ratingDate;
        this.comment = comment;
    }
}
