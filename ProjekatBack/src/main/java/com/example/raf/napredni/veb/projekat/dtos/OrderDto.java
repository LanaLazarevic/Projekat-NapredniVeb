package com.example.raf.napredni.veb.projekat.dtos;


import com.example.raf.napredni.veb.projekat.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {
    private Long id;
    private User createdBy;
    private String status;
    private boolean active;
    private LocalDateTime orderedAt;
    private String dishes;


}
