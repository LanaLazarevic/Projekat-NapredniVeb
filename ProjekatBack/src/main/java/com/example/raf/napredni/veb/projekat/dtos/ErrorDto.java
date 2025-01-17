package com.example.raf.napredni.veb.projekat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private Long errorId;
    private String forOrder;
    private String user;
    private String message;
    private LocalDateTime time;
    private String operation;
}
