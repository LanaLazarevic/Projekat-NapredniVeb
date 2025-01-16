package com.example.raf.napredni.veb.projekat.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "dish", nullable = false)
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "for_order", nullable = false)
    private Order order;

}
