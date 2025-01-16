package com.example.raf.napredni.veb.projekat.controllers;

import com.example.raf.napredni.veb.projekat.dtos.UserCreateDto;
import com.example.raf.napredni.veb.projekat.dtos.UserDto;
import com.example.raf.napredni.veb.projekat.dtos.UserUpdateDto;
import com.example.raf.napredni.veb.projekat.model.User;
import com.example.raf.napredni.veb.projekat.services.UserService;
import com.example.raf.napredni.veb.projekat.utils.MyUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_read_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_read_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDto userDto = null;
        try {
             userDto = userService.findUserByEmailAndReturnDto(email);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserCreateDto userCreateDto){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_create_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserUpdateDto userEditDto){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_update_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(userService.updateUser(userEditDto), HttpStatus.OK);
    }

    @PutMapping("/delete/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_delete_users"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            userService.deleteUser(email);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
