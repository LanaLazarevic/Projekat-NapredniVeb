package com.example.raf.napredni.veb.projekat.mappers;

import com.example.raf.napredni.veb.projekat.dtos.DishDto;
import com.example.raf.napredni.veb.projekat.model.Dish;
import org.springframework.stereotype.Component;


@Component
public class DishMapper {

    public DishDto dishToDishDto(Dish dish) {
        DishDto dishDto = new DishDto();
        dishDto.setId(dish.getDishId());
        dishDto.setName(dish.getName());
        dishDto.setPrice(dish.getPrice());
        return dishDto;
    }


}

