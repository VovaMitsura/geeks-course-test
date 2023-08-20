package com.example.geeksforlesstest.controller;

import com.example.geeksforlesstest.exception.ENotFoundException;
import com.example.geeksforlesstest.exception.EquationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(EquationException.class)
    public ErrorResponse handleEquationException(EquationException equationException) {
        return new ErrorResponse(equationException.getMessage());
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ENotFoundException.class)
    public ErrorResponse handleENotFoundException(ENotFoundException notFoundException){
        return new ErrorResponse(notFoundException.getMessage());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ErrorResponse {
        private String errorMessage;
    }
}
