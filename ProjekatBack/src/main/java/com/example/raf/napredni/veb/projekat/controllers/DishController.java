package com.example.raf.napredni.veb.projekat.controllers;

import com.example.raf.napredni.veb.projekat.dtos.DishDto;
import com.example.raf.napredni.veb.projekat.services.DishService;
import com.example.raf.napredni.veb.projekat.utils.MyUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService){
        this.dishService = dishService;
    }
    @GetMapping
    public ResponseEntity<List<DishDto>> getAllDishes(){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_search_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("{name}")
    public ResponseEntity<DishDto> getDishByName(@PathVariable String name){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_search_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        DishDto dishDto = dishService.findByName(name) .orElseThrow(() -> new UsernameNotFoundException("Dish not found with name: " + name));;

        return ResponseEntity.ok(dishDto);
    }


}
