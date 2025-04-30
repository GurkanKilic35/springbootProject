package com.example.webproje.dto;

import com.example.webproje.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieDTO {

    private long id;
    private String title;
    private LocalDate releaseDate;
    private String genre;
    private String description;
    private Type type;
    private float avgRate;
    private byte[] image;
    private String imageBase64;

    public MovieDTO(long id, String title, LocalDate releaseDate, String genre, String description, Type type, float avgRate, byte[] image) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.description = description;
        this.type = type;
        this.avgRate = avgRate;
        this.image = image;
    }
}
