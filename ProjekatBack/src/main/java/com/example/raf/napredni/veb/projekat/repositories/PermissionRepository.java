package com.example.raf.napredni.veb.projekat.repositories;

import com.example.raf.napredni.veb.projekat.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String email);
}
