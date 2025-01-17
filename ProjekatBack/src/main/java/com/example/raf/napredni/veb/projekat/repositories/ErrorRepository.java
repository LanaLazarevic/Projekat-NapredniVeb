package com.example.raf.napredni.veb.projekat.repositories;

import com.example.raf.napredni.veb.projekat.model.Error;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<Error, Long> {
    @Query("select error from Error error where error.user.email = ?1")
    Page<Error> findAllByEmail(Pageable pageable, String username);
}
