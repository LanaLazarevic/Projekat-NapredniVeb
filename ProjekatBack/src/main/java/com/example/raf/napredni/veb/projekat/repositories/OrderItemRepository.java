package com.example.raf.napredni.veb.projekat.repositories;

import com.example.raf.napredni.veb.projekat.model.Order;
import com.example.raf.napredni.veb.projekat.model.OrderItem;
import com.example.raf.napredni.veb.projekat.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
