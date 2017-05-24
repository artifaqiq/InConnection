/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.admin;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.user.RoleTypesSetDto;
import by.nc.lomako.dto.user.UserForCreateDto;
import by.nc.lomako.dto.user.UserForUpdateDto;
import by.nc.lomako.pojos.RoleType;
import by.nc.lomako.services.UserService;
import by.nc.lomako.services.exceptions.UniqueEmailException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController("adminUserController")
@RequestMapping("/api/v1/admin/users")
public class UserController {

    private final UserService userService;

    private final UserForCreateDto.DtoValidator userForCreateDtoValidator;

    private final UserForUpdateDto.DtoValidator userForUpdateDtoValidator;

    @Autowired
    public UserController(UserService userService, UserForCreateDto.DtoValidator userForCreateDtoValidator,
                          UserForUpdateDto.DtoValidator userForUpdateDtoValidator) {
        this.userService = userService;
        this.userForCreateDtoValidator = userForCreateDtoValidator;
        this.userForUpdateDtoValidator = userForUpdateDtoValidator;
    }

    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<OperationStatusDto> createUser(
            @RequestBody UserForCreateDto userDto,
            BindingResult bindingResult
    ) throws UniqueEmailException {

        userForCreateDtoValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))),
                    HttpStatus.BAD_REQUEST
            );
        }

        long userId = userService.create(userDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/users/" + userId);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.CREATED,
                        "success"
                ),
                headers,
                HttpStatus.CREATED
        );
    }

    @RequestMapping(value = "/{userId}/update", method = PUT)
    public ResponseEntity updateUser(
            @PathVariable("userId") String userIdString,
            @RequestBody UserForUpdateDto userDto,
            BindingResult bindingResult

    ) throws UserNotFoundException, UniqueEmailException {

        long userId = Long.parseLong(userIdString);


        System.out.println((userForUpdateDtoValidator.supports(userDto.getClass())));
        userForUpdateDtoValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))),
                    HttpStatus.BAD_REQUEST
            );
        }

        userService.update(userId, userDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/users/" + userId);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                headers,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{userId}/roles/update", method = PUT)
    public ResponseEntity updateUserRoles(
            @PathVariable("userId") String userIdString,
            @RequestBody RoleTypesSetDto roles
    ) throws UserNotFoundException {

        long userId = Long.parseLong(userIdString);

        userService.setRoles(userId, roles);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/search", method = GET)
    public ResponseEntity searchUserByRole(
            @RequestParam("role") String roleTypeString,
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString
    ) {
        int start = Integer.parseInt(startString);
        int limit = Integer.parseInt(limitString);

        if (!Arrays.stream(RoleType.values())
                .map(RoleType::toString)
                .collect(Collectors.toList())
                .contains(roleTypeString)) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.UNPROCESSABLE_ENTITY,
                            "Incorrect value of 'role' parameter"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        return new ResponseEntity<>(
                userService.findByRole(RoleType.valueOf(roleTypeString), start, limit),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{userId}", method = DELETE)
    public ResponseEntity deleteUser(
            @PathVariable long userId
    ) throws UserNotFoundException {

        userService.deleteById(userId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                HttpStatus.OK
        );
    }
}