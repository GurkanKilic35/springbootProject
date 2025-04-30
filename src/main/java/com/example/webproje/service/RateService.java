package com.example.webproje.service;

import com.example.webproje.dto.MovieDTO;
import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.RateEntity;
import com.example.webproje.mapper.MovieMapper;
import com.example.webproje.mapper.RateMapper;
import com.example.webproje.repository.RateRepo;
import com.example.webproje.service.interfaces.IRate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RateService implements IRate {
    RateRepo rateRepo;



    @Override
    public RateDTO addRate(RateDTO rateDTO) {
        try {
            RateEntity rateEntity = RateMapper.dtoToEntity(rateDTO);

            rateRepo.save(rateEntity);

            return rateDTO;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Veritabanına kaydederken bir hata oluştu: " + e.getMessage());
        }
    }
    @Override
    public RateDTO updateRate(RateDTO rateDTO) {
        RateEntity rateEntity = rateRepo.findById(rateDTO.getId()).get();
        rateEntity.setScore(rateDTO.getScore());
        rateEntity.setRatingDate(rateDTO.getRatingDate());
        rateEntity.setComment(rateDTO.getComment());

        rateRepo.save(rateEntity);
        return rateDTO;
    }

    @Override
    public List<RateDTO> findAllRate() {
        return rateRepo.findAll().stream()
                .map(RateMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RateDTO> findRateById(Long id) {
        return rateRepo.findById(id).map(RateMapper::entityToDTO);
    }

    @Override
    public void deleteRate(Long id) {
        rateRepo.deleteById(id);
    }
}
