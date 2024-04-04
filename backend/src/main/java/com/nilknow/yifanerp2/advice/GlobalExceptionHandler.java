package com.nilknow.yifanerp2.advice;

import com.nilknow.yifanerp2.controller.Res;
import com.nilknow.yifanerp2.exception.ResException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResException.class)
    public ResponseEntity<Res<?>> handleException(Exception e) {
        return ResponseEntity.ok(new Res<>().fail(e.getMessage()));
    }
}