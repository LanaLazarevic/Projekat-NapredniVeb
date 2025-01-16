package com.example.raf.napredni.veb.projekat.responses;

import com.example.raf.napredni.veb.projekat.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private Set<String> permissions;

}
