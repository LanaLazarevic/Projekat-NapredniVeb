package com.example.raf.napredni.veb.projekat.dtos;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
   private Long id;
   private String firstname;
   private String lastname;
   private String email;
   private Set<String> permissions;

}
