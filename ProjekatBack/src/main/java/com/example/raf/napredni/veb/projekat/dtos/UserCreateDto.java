package com.example.raf.napredni.veb.projekat.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateDto {

    @NotBlank(message = "First name is required!")
    private String firstname;
    @NotBlank(message = "Last name is required!")
    private String lastname;
    @Email(message = "Email should be valid!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
    private Set<String> permissions;

}
