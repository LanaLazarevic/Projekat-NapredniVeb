package com.example.raf.napredni.veb.projekat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderScheduleDto {
    private List<String> dishes;
    private LocalDateTime orderTime;
}
