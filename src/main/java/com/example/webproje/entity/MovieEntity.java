package com.example.webproje.entity;

import com.example.webproje.enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Başlık boş olamaz.")
    @Size(max = 255, message = "Başlık en fazla 255 karakter olabilir.")
    @Column(nullable = false)
    private String title;
    private LocalDate releaseDate;
    private String genre;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir.")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Tür boş olamaz.")
    @Enumerated(EnumType.STRING)
    private Type type;
    private float avgRate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<RateEntity> rate;

    public MovieEntity(long id, String title, LocalDate releaseDate, String genre, String description, Type type, float avgRate, byte[] image) {
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
