package com.bmsnc.common;


import com.bmsnc.common.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class, MethodArgumentNotValidException.class})
    public Result<String> handleBadRequestExceptions(Exception e){
        log.error("RuntimeException occurred: {}", e.getMessage(), e);
        return Result.of("", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}
