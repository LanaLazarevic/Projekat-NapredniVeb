package com.example.raf.napredni.veb.projekat.services;

import com.example.raf.napredni.veb.projekat.dtos.OrderCreateDto;
import com.example.raf.napredni.veb.projekat.dtos.OrderDto;
import com.example.raf.napredni.veb.projekat.dtos.OrderFilterDto;
import com.example.raf.napredni.veb.projekat.dtos.OrderCancelDto;
import com.example.raf.napredni.veb.projekat.filters.OrderFilter;
import com.example.raf.napredni.veb.projekat.mappers.DishMapper;
import com.example.raf.napredni.veb.projekat.mappers.OrderMapper;
import com.example.raf.napredni.veb.projekat.model.*;
import com.example.raf.napredni.veb.projekat.repositories.DishRepository;
import com.example.raf.napredni.veb.projekat.repositories.OrderItemRepository;
import com.example.raf.napredni.veb.projekat.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper, DishRepository dishRepository, DishMapper dishMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    public Page<OrderDto> searchOrders(Integer page, Integer size, OrderFilterDto orderFilterDto) {
        Specification<Order> spec = Specification.where(orderFilterDto.getFrom() != null ? OrderFilter.withDateFrom(orderFilterDto.getFrom()) : null)
                .and(orderFilterDto.getTo() != null ? OrderFilter.withDateTo(orderFilterDto.getTo()) : null)
                .and(orderFilterDto.getEmail() != null ? OrderFilter.byEmail(orderFilterDto.getEmail()) : null)
                .and(!Objects.isNull(orderFilterDto.getStatuses()) && !orderFilterDto.getStatuses().isEmpty() ? OrderFilter.withStatuses(orderFilterDto.getStatuses()) : null);
        return orderRepository.findAll(spec, PageRequest.of(page, size, Sort.by("orderId").descending())).map(orderMapper::orderToOrderDto);
    }

    public OrderDto createOrder(OrderCreateDto orderCreateDto, User user) {
        List<Status> orderStatuses = new ArrayList<>(List.of(Status.PREPARING, Status.IN_DELIVERY));
        if(orderRepository.numberOfOrdersInProgress(orderStatuses)==3){

        }
        Order order = new Order();
        order.setCreatedBy(user);
        orderRepository.save(order);
        for (String dish : orderCreateDto.getDishes()) {
            Dish dish1 = dishRepository.findByName(dish).orElseThrow(() -> new RuntimeException("Dish not found"));
            OrderItem orderItem = new OrderItem(dish1,order);
            order.getOrderItems().add(orderItem);
            orderItemRepository.save(orderItem);
        }
        updateStatus(order);
        return orderMapper.orderToOrderDto(order);
    }

    @Async
    @Transactional
    protected void scheduleStatusUpdate(Order order, Status nextStatus, long delayInSeconds, Runnable nextTask) {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            order.setStatus(nextStatus);
            if(nextStatus.equals(Status.DELIVERED))
                order.setActive(false);
            orderRepository.save(order);
            if (nextTask != null) {
                nextTask.run();
            }
        }, delayInSeconds, TimeUnit.SECONDS);
    }

    private int getRandomDelay(int maxDeviation) {
        return (int) (Math.random() * maxDeviation);
    }

    @Transactional
    public void updateStatus(Order order) {
        scheduleStatusUpdate(order,Status.PREPARING,10 + getRandomDelay(3), () ->
                scheduleStatusUpdate(order , Status.IN_DELIVERY, 15+ getRandomDelay(3), ()->
                        scheduleStatusUpdate(order,Status.DELIVERED,20+getRandomDelay(3), null)));
    }

    public OrderDto cancelOrder(OrderCancelDto orderCancelDto){
        Order order = orderRepository.findById(orderCancelDto.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!(order.getStatus().equals(Status.ORDERED) || order.getStatus().equals(Status.SCHEDULED)) && order.getStatus().equals(Status.CANCELED)){

        }
        order=orderMapper.orderEditDtoToOrder(order, orderCancelDto);
        orderRepository.save(order);
        return orderMapper.orderToOrderDto(order);
    }
}
