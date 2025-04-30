package com.example.webproje.service;

import com.example.webproje.dto.UserDTO;
import com.example.webproje.entity.UserEntity;
import com.example.webproje.mapper.UserMapper;
import com.example.webproje.repository.UserRepo;
import com.example.webproje.service.interfaces.IUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUser {
    private PasswordEncoder passwordEncoder;
    UserRepo userRepo;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setRole(userDTO.getRole());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userEntity = userRepo.save(userEntity);
        return UserMapper.entityToDTO(userEntity);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepo.findAll().stream()
                .map(UserMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findUserById(Long id) {
        return userRepo.findById(id).map(UserMapper::entityToDTO);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity userEntity = userRepo.findById(userDTO.getId()).get();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());

        userRepo.save(userEntity);
        return userDTO;
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public UserEntity findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
