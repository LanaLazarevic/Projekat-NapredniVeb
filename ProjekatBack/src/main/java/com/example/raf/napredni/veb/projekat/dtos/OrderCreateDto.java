package com.example.raf.napredni.veb.projekat.dtos;


import com.example.raf.napredni.veb.projekat.model.Dish;
import com.example.raf.napredni.veb.projekat.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreateDto {

    private List<String> dishes;


}
