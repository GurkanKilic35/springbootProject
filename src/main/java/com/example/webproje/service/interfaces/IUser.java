package com.example.webproje.service.interfaces;

import com.example.webproje.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUser {
    UserDTO registerUser(UserDTO userDTO);
    List<UserDTO> findAllUsers();
    Optional<UserDTO> findUserById(Long id);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);
}
