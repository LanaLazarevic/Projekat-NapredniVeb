package com.example.raf.napredni.veb.projekat.filters;

import com.example.raf.napredni.veb.projekat.model.Order;
import com.example.raf.napredni.veb.projekat.model.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFilter {
    public static Specification<Order> withDateFrom(LocalDateTime dateFrom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("orderedAt"), dateFrom);
    }

    public static Specification<Order> withDateTo(LocalDateTime dateTo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("orderedAt"), dateTo);
    }

    public static Specification<Order> byEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy").get("email"), email);
    }

    public static Specification<Order> withStatuses(List<Status> statuses) {
        return (root, query, criteriaBuilder) ->
                root.get("status").in(statuses);
    }
}
