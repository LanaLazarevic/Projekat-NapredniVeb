package com.example.raf.napredni.veb.projekat.services;


import com.example.raf.napredni.veb.projekat.dtos.ErrorDto;
import com.example.raf.napredni.veb.projekat.mappers.ErrorMapper;
import com.example.raf.napredni.veb.projekat.repositories.ErrorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
@Data
public class ErrorService {

    private final ErrorMapper errorMapper;
    private final ErrorRepository errorRepository;

    public Page<ErrorDto> findAll(Integer page, Integer size) {
        return errorRepository.findAll(PageRequest.of(page, size, Sort.by("errorId").descending())).map(errorMapper :: errorToErrorDto);
    }

    public Page<ErrorDto> findAllByUsername(Integer page, Integer size, String email) {
        return errorRepository.findAllByEmail(PageRequest.of(page, size, Sort.by("errorId").descending()),email).map(errorMapper :: errorToErrorDto);
    }

}
