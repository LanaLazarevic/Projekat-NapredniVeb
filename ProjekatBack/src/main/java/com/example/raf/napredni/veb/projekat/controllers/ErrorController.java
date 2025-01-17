package com.example.raf.napredni.veb.projekat.controllers;


import com.example.raf.napredni.veb.projekat.dtos.ErrorDto;
import com.example.raf.napredni.veb.projekat.services.ErrorService;
import com.example.raf.napredni.veb.projekat.utils.MyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/errors")
public class ErrorController {

    private ErrorService errorService;

    public ErrorController(ErrorService errorService){
        this.errorService = errorService;
    }

    @GetMapping
    public ResponseEntity<Page<ErrorDto>> getAllErrors(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size){
        System.out.println("page: " + page + " size: " + size);
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("can_place_order"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(userDetails.isAdmin())
            return ResponseEntity.ok(errorService.findAll(page, size));
        return ResponseEntity.ok(errorService.findAllByUsername(page, size, userDetails.getEmail()));
    }

}
