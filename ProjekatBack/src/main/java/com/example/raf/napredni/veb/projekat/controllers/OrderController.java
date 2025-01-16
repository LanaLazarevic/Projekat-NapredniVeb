package com.example.raf.napredni.veb.projekat.controllers;

import com.example.raf.napredni.veb.projekat.dtos.*;
import com.example.raf.napredni.veb.projekat.services.DishService;
import com.example.raf.napredni.veb.projekat.services.OrderService;
import com.example.raf.napredni.veb.projekat.utils.MyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/search")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "5") Integer size,
                                                       @RequestBody OrderFilterDto orderFilterDto){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_search_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(!userDetails.isAdmin())
            orderFilterDto.setEmail(userDetails.getEmail());
        return ResponseEntity.ok(orderService.searchOrders(page, size, orderFilterDto));
    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderCreateDto orderCreateDto){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_place_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        OrderDto orderDto = orderService.createOrder(orderCreateDto, userDetails.getUser());
        if(orderDto != null)
            return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
        return ResponseEntity.status(429).build();
    }

    @PutMapping
    public ResponseEntity<OrderDto> cancelOrder(@RequestBody OrderCancelDto orderCancelDto){
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_cancel_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(orderService.cancelOrder(orderCancelDto), HttpStatus.OK);
    }


}
