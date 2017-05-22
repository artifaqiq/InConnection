/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.exception;

import by.nc.lomako.dto.OperationStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Lomako
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<?> numberFormatExceptionHandler(Exception e) {
        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.BAD_REQUEST,
                        "Bad request: " + e.getMessage() + "; " + e.getClass().getName()),
                HttpStatus.BAD_REQUEST
        );
    }
}
