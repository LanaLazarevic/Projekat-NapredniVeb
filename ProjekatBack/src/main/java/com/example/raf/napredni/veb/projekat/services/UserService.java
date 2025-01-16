package com.example.raf.napredni.veb.projekat.services;

import com.example.raf.napredni.veb.projekat.dtos.UserCreateDto;
import com.example.raf.napredni.veb.projekat.dtos.UserDto;
import com.example.raf.napredni.veb.projekat.dtos.UserUpdateDto;
import com.example.raf.napredni.veb.projekat.mappers.UserMapper;
import com.example.raf.napredni.veb.projekat.model.Permission;
import com.example.raf.napredni.veb.projekat.model.User;
import com.example.raf.napredni.veb.projekat.repositories.PermissionRepository;
import com.example.raf.napredni.veb.projekat.repositories.UserRepository;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.permissionRepository = permissionRepository;
    }

    public UserDto findUserByEmailAndReturnDto(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return userMapper.userToUserDto(user);
    }

    public User findUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user;
    }


    public UserDto createUser(UserCreateDto userDto) {
        User user = userMapper.userCreateDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }


    public List<UserDto> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDto) // Metoda za mapiranje User u UserDto
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User maybeUser = findUserByEmail(userUpdateDto.getEmail());
        if(maybeUser != null && !Objects.equals(maybeUser.getUserId(), user.getUserId())){
            throw new RuntimeException("User with email " + userUpdateDto.getEmail() + " already exists");
        }

        user.setFirstname(userUpdateDto.getFirstname());
        user.setLastname(userUpdateDto.getLastname());
        user.setEmail(userUpdateDto.getEmail());
        if (userUpdateDto.getPermissions() != null) {
            Set<Permission> permissions = userUpdateDto.getPermissions().stream()
                    .map(permissionName -> permissionRepository.findByName(permissionName)
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName)))
                    .collect(Collectors.toSet());
            user.setPermissions(permissions);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.userToUserDto(updatedUser);
    }



    public void deleteUser(String email) throws UsernameNotFoundException{
        User user = findUserByEmail(email);
        System.out.println(user.getUserId());
        userRepository.deleteById(user.getUserId());
    }
}
