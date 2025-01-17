package com.example.raf.napredni.veb.projekat.mappers;


import com.example.raf.napredni.veb.projekat.dtos.ErrorDto;
import com.example.raf.napredni.veb.projekat.model.Error;
import org.springframework.stereotype.Component;


@Component
public class ErrorMapper {

    public ErrorDto errorToErrorDto(Error error) {

        ErrorDto errorDto = new ErrorDto();
        errorDto.setOperation(error.getOperation());
        errorDto.setErrorId(error.getErrorId());
        errorDto.setMessage(error.getMessage());
        errorDto.setForOrder(error.getForOrder());
        errorDto.setTime(error.getTime());
        errorDto.setUser(error.getUser().getEmail());

        return errorDto;
    }

}
