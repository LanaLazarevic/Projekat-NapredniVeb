package com.example.raf.napredni.veb.projekat.controllers;

import com.example.raf.napredni.veb.projekat.model.Permission;
import com.example.raf.napredni.veb.projekat.model.User;
import com.example.raf.napredni.veb.projekat.requests.LoginRequest;
import com.example.raf.napredni.veb.projekat.responses.LoginResponse;
import com.example.raf.napredni.veb.projekat.services.UserService;
import com.example.raf.napredni.veb.projekat.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        User user = null;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            user = userService.findUserByEmail(loginRequest.getEmail());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(
                new LoginResponse(
                        jwtUtil.generateJwtToken(loginRequest.getEmail(),user.getPermissions().stream()
                                .map(Permission::getName)
                                .collect(Collectors.toSet())),
                        user.getPermissions().stream()
                                .map(Permission::getName)
                                .collect(Collectors.toSet())
                ));
    }
}
