package com.example.raf.napredni.veb.projekat.scheduler;

import com.example.raf.napredni.veb.projekat.dtos.OrderDto;
import com.example.raf.napredni.veb.projekat.model.Order;
import com.example.raf.napredni.veb.projekat.model.Status;
import com.example.raf.napredni.veb.projekat.repositories.OrderRepository;
import com.example.raf.napredni.veb.projekat.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Component
public class ScheduleOrder {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Scheduled(cron = "0 * * * * *")
    public void scheduleOrderManager() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        List<Order> scheduleOrders = orderRepository.findAllOrdersWithStatus(Status.SCHEDULED);
        for (Order order : scheduleOrders) {
            if (order.getOrderedAt().isBefore(dateTimeNow) && order.isActive()) {
                OrderDto orderDto = orderService.makeScheduledOrder(order);
                if (orderDto == null) {
                    System.out.println("Nije napravio");
                }else{
                    System.out.println("Jeste napravio" + orderDto.getStatus());
                }
            }
        }

    }
}
