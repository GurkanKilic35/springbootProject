package com.example.webproje.service.interfaces;

import com.example.webproje.dto.RateDTO;

import java.util.List;
import java.util.Optional;


public interface IRate {
    RateDTO addRate(RateDTO rateDTO);
    RateDTO updateRate(RateDTO rateDTO);
    List<RateDTO> findAllRate();
    Optional<RateDTO> findRateById(Long id);
    void deleteRate(Long id);

}
