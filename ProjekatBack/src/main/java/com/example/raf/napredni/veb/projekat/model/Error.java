package com.example.raf.napredni.veb.projekat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "errors")
public class Error {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "error_id")
    private Long errorId;
    @Column(name = "for_order")
    private String forOrder;
    private String message;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user")
    private User user;
    private LocalDateTime time;
    private String operation;

}
