/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.exception;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Lomako
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<Object>(
                new OperationStatusDto(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<?> messageNotFoundExceptionHandler(MessageNotFoundException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.NOT_FOUND,
                        "Message not found"),
                HttpStatus.BAD_REQUEST
        );
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> numberFormatExceptionHandler(NumberFormatException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage() + "; Required digit value"),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<?> notBindExceptionHandler(Exception e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "DOSNT BINDED EXCEPTION "),
                HttpStatus.BAD_REQUEST
        );
    }


}
