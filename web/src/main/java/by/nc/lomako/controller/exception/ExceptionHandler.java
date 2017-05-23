/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.exception;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.PostNotFoundException;
import by.nc.lomako.exceptions.UniqueEmailException;
import by.nc.lomako.exceptions.UserNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
                        "Message not found"
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> postNotFoundExceptionHandler(PostNotFoundException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.NOT_FOUND,
                        "Post not found"
                ),
                HttpStatus.BAD_REQUEST
        );
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> numberFormatExceptionHandler(NumberFormatException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage() + "; Required digit value"
                ),
                HttpStatus.BAD_REQUEST
        );
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(UniqueEmailException.class)
    public ResponseEntity<?> incorrectRequestFormatHandler(UniqueEmailException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.CONFLICT,
                        "User with some email already exist"
                ),
                HttpStatus.CONFLICT
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> missingRequestParameterHandler(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Missed required request parameters"
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> incorrectRequestFormatHandler(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Incorrect request body format"
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> StartAndLimitValuesForPaginationFormatHandler(InvalidDataAccessApiUsageException e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.BAD_REQUEST,
                        "'start' and 'limit' parameters must be greater or equals 0"
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<?> notBindExceptionHandler(Exception e) {
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "DOSNT BINDED EXCEPTION " + e.getClass()
                ),
                HttpStatus.BAD_REQUEST
        );
    }


}
