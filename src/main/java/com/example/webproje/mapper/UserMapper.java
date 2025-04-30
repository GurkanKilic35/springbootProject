package com.example.webproje.mapper;

import com.example.webproje.dto.UserDTO;
import com.example.webproje.entity.UserEntity;

public class UserMapper {
    public  static UserDTO entityToDTO(UserEntity userEntity){
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }
    public  static UserEntity dtoToEntity(UserDTO userDTO){
        return new UserEntity(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRole()
        );
    }
}
