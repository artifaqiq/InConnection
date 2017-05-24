/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.exceptions.MessageNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.security.details.UserDetailsImpl;
import by.nc.lomako.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    private final MessageForSendDto.DtoValidator messageForSendDtoValidator;

    @Autowired
    public MessageController(MessageService messageService, MessageForSendDto.DtoValidator messageForSendDtoValidator) {
        this.messageService = messageService;
        this.messageForSendDtoValidator = messageForSendDtoValidator;
    }

    @RequestMapping(value = "/send", method = POST)
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageForSendDto messageDto,
            Principal principal,
            BindingResult bindingResult

    ) throws NumberFormatException, UserNotFoundException {

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        messageForSendDtoValidator.validate(messageDto, bindingResult);
        if (bindingResult.hasErrors()) {

            System.out.println(bindingResult.getAllErrors());
            System.out.println();

            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.BAD_REQUEST,
                            bindingResult.getFieldErrors().stream()
                                    .map(ObjectError::getCode)
                                    .collect(Collectors.joining("; "))
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        long messageId = messageService.sendMessage(currentUserId, messageDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/api/v1/messages/" + messageId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                headers,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/delete/{messageId}", method = DELETE)
    public ResponseEntity<?> deleteMessage(
            @PathVariable("messageId") String messageIdString,
            Principal principal
    ) throws MessageNotFoundException {
        long messageId = Long.parseLong(messageIdString);

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        MessageInfoDto message = messageService.findById(messageId);
        if (message.getUserFromId() != currentUserId && message.getUserToId() != currentUserId) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.FORBIDDEN,
                            "Access denied"
                    ),
                    HttpStatus.FORBIDDEN
            );
        }

        messageService.deleteById(messageId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/last/{userId}", method = GET)
    public ResponseEntity<List<MessageInfoDto>> getLastMessagesBetweenUsers(
            @PathVariable("userId") String userIdString,
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString,
            Principal principal
    ) throws UserNotFoundException {

        long userId = Long.parseLong(userIdString);
        int start = Integer.parseInt(startString);
        int limit = Integer.parseInt(limitString);

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        List<MessageInfoDto> messages = messageService.findLastByUsers(userId, currentUserId, start, limit);

        return new ResponseEntity<>(
                messages,
                HttpStatus.OK
        );

    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<?> showMessage(
            @PathVariable("id") String idString,
            Principal principal
    ) throws MessageNotFoundException {

        long id = Long.parseLong(idString);

        MessageInfoDto messageInfoDto = messageService.findById(id);

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        if (messageInfoDto.getUserFromId() != currentUserId && messageInfoDto.getUserToId() != currentUserId) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.FORBIDDEN,
                            "Access denied"
                    ),
                    HttpStatus.FORBIDDEN
            );
        }

        return new ResponseEntity<>(
                messageInfoDto,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/last_dialogs/index", method = GET)
    public ResponseEntity<List<MessageInfoDto>> getLastDialogs(
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString,
            Principal principal
    ) throws UserNotFoundException {

        int start = Integer.parseInt(startString);
        int limit = Integer.parseInt(limitString);

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        List<MessageInfoDto> lastDialogs = messageService.findLastDialogs(currentUserId, start, limit);

        return new ResponseEntity<List<MessageInfoDto>>(
                lastDialogs,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/last_dialogs/count", method = GET)
    public ResponseEntity<?> countLastDialogs(
            Principal principal
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        long countDialogs = messageService.countDialogs(currentUserId);

        return new ResponseEntity<Object>(
                countDialogs,
                HttpStatus.OK
        );
    }

}
