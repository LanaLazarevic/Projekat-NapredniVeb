package com.example.raf.napredni.veb.projekat.dtos;

import com.example.raf.napredni.veb.projekat.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderFilterDto {

   private LocalDateTime from;
   private LocalDateTime to;
   private String email;
   private List<Status> statuses;
}
