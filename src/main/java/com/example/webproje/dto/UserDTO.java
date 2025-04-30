package com.example.webproje.dto;

import com.example.webproje.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;
    private String username;
    private String password;
    private String email;
    private Role role = Role.USER;
}
