package com.example.raf.napredni.veb.projekat.mappers;


import com.example.raf.napredni.veb.projekat.dtos.UserCreateDto;
import com.example.raf.napredni.veb.projekat.dtos.UserDto;
import com.example.raf.napredni.veb.projekat.dtos.UserUpdateDto;
import com.example.raf.napredni.veb.projekat.model.Permission;
import com.example.raf.napredni.veb.projekat.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getUserId());
        userDto.setPermissions(user.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet()));
        return userDto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setFirstname(userCreateDto.getFirstname());
        user.setLastname(userCreateDto.getLastname());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        if (userCreateDto.getPermissions() != null) {
            Set<Permission> permissions = userCreateDto.getPermissions().stream()
                    .map(permissionName -> {
                        Permission permission = new Permission();
                        permission.setName(permissionName);
                        return permission;
                    })
                    .collect(Collectors.toSet());
            user.setPermissions(permissions);
        }
        return user;
    }




    public User userUpdateDtoToUser(User user, UserUpdateDto userUpdateDto) {
        user.setFirstname(userUpdateDto.getFirstname());
        user.setLastname(userUpdateDto.getLastname());
        user.setEmail(userUpdateDto.getEmail());
        if (userUpdateDto.getPermissions() != null) {
            Set<Permission> permissions = userUpdateDto.getPermissions().stream()
                    .map(permissionName -> {
                        Permission permission = new Permission();
                        permission.setName(permissionName);
                        return permission;
                    })
                    .collect(Collectors.toSet());
            user.setPermissions(permissions);
        }
        return user;
    }
}

