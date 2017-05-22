/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.message.MessageForSendDto;
import by.nc.lomako.dto.message.MessageInfoDto;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.security.UserDetailsImpl;
import by.nc.lomako.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/send", method = POST)
    public ResponseEntity<?> sentMessage(
            @RequestBody MessageForSendDto messageDto,
            Principal principal

    ) throws NumberFormatException, UserNotFoundException {

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication)principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        long messageId = messageService.sendMessage(currentUserId, messageDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/api/v1/messages/" + messageId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                headers,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/deleteById/{id}", method = DELETE)
    public ResponseEntity<?> deleteMessage(
            @PathVariable("id") String messageIdString,
            Principal principal
    ) {
        long messageId = Long.valueOf(messageIdString);
        // TODO: 5/17/17

        messageService.deleteById(messageId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/last/{userId}", method = GET)
    public ResponseEntity<?> lastMessages(
            @PathVariable("userId") String userIdString,
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString,
            Principal principal
    ) {
        long userId = Long.valueOf(userIdString);
        int start = Integer.valueOf(startString);
        int limit = Integer.valueOf(limitString);

        if(start < 0 || limit < 0) {
            throw new NumberFormatException();
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication)principal).getPrincipal();
        long currentUserId = userDetails.getUser().getId();

        List<MessageInfoDto> messages = messageService.findLastByUsers(userId, currentUserId, start, limit);

        return new ResponseEntity<>(
                messages,
                HttpStatus.OK
        );

    }
}
