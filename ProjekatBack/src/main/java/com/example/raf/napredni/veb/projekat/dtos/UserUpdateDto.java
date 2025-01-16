package com.example.raf.napredni.veb.projekat.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private Long id;
    @NotBlank(message = "First name is required!")
    private String firstname;
    @NotBlank(message = "Last name is required!")
    private String lastname;
    @Email(message = "Email should be valid!")
    private String email;
    private Set<String> permissions;

}
