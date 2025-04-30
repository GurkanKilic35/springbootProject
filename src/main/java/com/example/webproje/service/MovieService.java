package com.example.webproje.service;

import com.example.webproje.dto.MovieDTO;
import com.example.webproje.entity.MovieEntity;
import com.example.webproje.mapper.MovieMapper;
import com.example.webproje.repository.MovieRepo;
import com.example.webproje.service.interfaces.IMovie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService implements IMovie {
    MovieRepo movieRepo;

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        MovieEntity movieEntity = MovieMapper.dtoToEntity(movieDTO);
        movieRepo.save(movieEntity);
        return movieDTO;
    }

    @Override
    public List<MovieDTO> findAllMovie() {
        return movieRepo.findAll().stream()
                .map(MovieMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDTO> findMovieById(Long id) {
        return movieRepo.findById(id).map(MovieMapper::entityToDTO);
    }

    @Override
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        MovieEntity movieEntity = movieRepo.findById(movieDTO.getId()).get();
        movieEntity.setTitle(movieDTO.getTitle());
        movieEntity.setReleaseDate(movieDTO.getReleaseDate());
        movieEntity.setGenre(movieDTO.getGenre());
        movieEntity.setDescription(movieDTO.getDescription());
        movieEntity.setType(movieDTO.getType());
        movieEntity.setImage(movieDTO.getImage());

        movieRepo.save(movieEntity);
        return movieDTO;
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepo.deleteById(id);
    }
}
