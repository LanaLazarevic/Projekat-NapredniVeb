package com.example.raf.napredni.veb.projekat.services;

import com.example.raf.napredni.veb.projekat.dtos.DishDto;
import com.example.raf.napredni.veb.projekat.dtos.UserDto;
import com.example.raf.napredni.veb.projekat.mappers.DishMapper;
import com.example.raf.napredni.veb.projekat.model.Dish;
import com.example.raf.napredni.veb.projekat.model.User;
import com.example.raf.napredni.veb.projekat.repositories.DishRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public DishService(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    public Optional<DishDto> findByName(String name) {
        return dishRepository.findByName(name).map(dishMapper::dishToDishDto);
    }

    public List<DishDto> getAllDishes() {
        List<Dish> dishes = (List<Dish>) dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::dishToDishDto)
                .collect(Collectors.toList());
    }
}
