package com.example.webproje.mapper;

import com.example.webproje.dto.MovieDTO;

import com.example.webproje.entity.MovieEntity;


public class MovieMapper {
    public  static MovieDTO entityToDTO(MovieEntity movieEntity){
        return new MovieDTO(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getReleaseDate(),
                movieEntity.getGenre(),
                movieEntity.getDescription(),
                movieEntity.getType(),
                movieEntity.getAvgRate(),
                movieEntity.getImage()

        );
    }
    public  static MovieEntity dtoToEntity(MovieDTO movieDTO){
        return new MovieEntity(
                movieDTO.getId(),
                movieDTO.getTitle(),
                movieDTO.getReleaseDate(),
                movieDTO.getGenre(),
                movieDTO.getDescription(),
                movieDTO.getType(),
                movieDTO.getAvgRate(),
                movieDTO.getImage()
        );
    }
}
