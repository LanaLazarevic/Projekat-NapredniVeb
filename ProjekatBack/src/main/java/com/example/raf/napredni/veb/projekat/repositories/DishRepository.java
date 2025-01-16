package com.example.raf.napredni.veb.projekat.repositories;

import com.example.raf.napredni.veb.projekat.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findByName(String email);
}
