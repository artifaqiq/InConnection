/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.admin;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageForUpdateDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController("adminMessageController")
@RequestMapping("/api/v1/admin/messages")
public class MessageController {

    private MessageService messageService;

    private MessageForSendDto.DtoValidator messageForSendDtoValidator;

    private MessageForUpdateDto.DtoValidator messageForUpdateDtoValidator;

    @Autowired
    public MessageController(MessageService messageService, MessageForSendDto.DtoValidator messageForSendDtoValidator,
                             MessageForUpdateDto.DtoValidator messageForUpdateDtoValidator) {
        this.messageService = messageService;
        this.messageForSendDtoValidator = messageForSendDtoValidator;
        this.messageForUpdateDtoValidator = messageForUpdateDtoValidator;
    }

    @RequestMapping("/user/{userId}/create")
    public ResponseEntity<?> createMessage(
            @PathVariable("userId") String userIdString,
            @RequestBody MessageForSendDto messageDto,
            BindingResult bindingResult
    ) throws UserNotFoundException {
        long userId = Long.parseLong(userIdString);

        messageForSendDtoValidator.validate(messageDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        long messageId = messageService.sendMessage(userId, messageDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/admin/messages/" + messageId);
        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.CREATED,
                        "success"
                ),
                headers,
                HttpStatus.CREATED
        );
    }

    @RequestMapping(value = "/{messageId}", method = GET)
    public ResponseEntity<?> showMessage(
            @PathVariable("messageId") String messageIdString
    ) throws MessageNotFoundException {
        long messageId = Long.parseLong(messageIdString);

        MessageInfoDto messageInfoDto = messageService.findById(messageId);

        return new ResponseEntity<>(
                messageInfoDto,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{messageId}/update", method = PUT)
    public ResponseEntity<?> updateMessage(
            @PathVariable("messageId") String messageIdString,
            @RequestBody MessageForUpdateDto messageForUpdateDto,
            BindingResult bindingResult
    ) throws MessageNotFoundException, UserNotFoundException {

        long messageId = Long.parseLong(messageIdString);

        messageForUpdateDtoValidator.validate(messageForUpdateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        messageService.update(messageId, messageForUpdateDto);

        return new ResponseEntity<Object>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{messageId}", method = DELETE)
    public ResponseEntity<?> deleteMessage(
            @PathVariable("messageId") String messageIdString
    ) throws MessageNotFoundException {

        long messageId = Long.parseLong(messageIdString);

        messageService.deleteById(messageId);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/index", method = GET)
    public ResponseEntity<?> findAll() {
        List<MessageInfoDto> messages = messageService.findAll();

        return new ResponseEntity<>(
                messages,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/count", method = GET)
    public ResponseEntity<?> count() {
        long count = messageService.count();

        return new ResponseEntity<>(
                count,
                HttpStatus.OK
        );
    }
}
