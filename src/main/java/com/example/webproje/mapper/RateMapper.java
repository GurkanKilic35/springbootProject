package com.example.webproje.mapper;

import com.example.webproje.dto.RateDTO;
import com.example.webproje.entity.RateEntity;

public class RateMapper {
    public  static RateDTO entityToDTO(RateEntity rateEntity){
        return new RateDTO(
                rateEntity.getId(),
                rateEntity.getUser(),
                rateEntity.getMovie(),
                rateEntity.getScore(),
                rateEntity.getRatingDate(),
                rateEntity.getComment()
        );
    }
    public static RateEntity dtoToEntity(RateDTO rateDTO) {
        RateEntity rateEntity = new RateEntity();
        rateEntity.setUser(rateDTO.getUser());
        rateEntity.setMovie(rateDTO.getMovie());
        rateEntity.setScore(rateDTO.getScore());
        rateEntity.setRatingDate(rateDTO.getRatingDate());
        rateEntity.setComment(rateDTO.getComment());
        return rateEntity;
    }
}
