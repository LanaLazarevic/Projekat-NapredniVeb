package com.example.raf.napredni.veb.projekat.repositories;

import com.example.raf.napredni.veb.projekat.model.Order;
import com.example.raf.napredni.veb.projekat.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("select count(order) from Order order where order.status in (:statuses)")
    Integer numberOfOrdersInProgress(@Param("statuses") List<Status> statuses);

    @Query("select order from Order order where order.status = :status")
    List<Order> findAllOrdersWithStatus(@Param("status") Status status);
}
