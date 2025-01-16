package com.example.raf.napredni.veb.projekat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
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


    public OrderItem(Dish dish, Order order) {
        this.dish = dish;
        this.order = order;
    }
}
