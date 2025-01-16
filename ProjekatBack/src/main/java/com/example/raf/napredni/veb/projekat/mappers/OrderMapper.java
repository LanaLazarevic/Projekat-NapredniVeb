package com.example.raf.napredni.veb.projekat.mappers;

import com.example.raf.napredni.veb.projekat.dtos.OrderDto;
import com.example.raf.napredni.veb.projekat.dtos.OrderCancelDto;
import com.example.raf.napredni.veb.projekat.model.Order;
import com.example.raf.napredni.veb.projekat.model.Status;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class OrderMapper {

    public OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getOrderId());
        orderDto.setActive(order.isActive());
        orderDto.setCreatedBy(order.getCreatedBy());
        orderDto.setStatus(String.valueOf(order.getStatus()));
        orderDto.setOrderedAt(order.getOrderedAt());
        String dishes = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getDish().toString())
                .collect(Collectors.joining(", "));
        orderDto.setDishes(dishes);
        return orderDto;
    }

    public Order orderEditDtoToOrder(Order order, OrderCancelDto orderCancelDto) {
        order.setStatus(Status.CANCELED);
        order.setActive(false);
        return order;
    }
}
