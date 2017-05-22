/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.exceptions.PostNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.security.UserDetailsImpl;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<?> create(
            @RequestBody PostForCreateDto postDto
    ) throws UserNotFoundException {

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = user.getUser().getId();
        long postId = postService.create(postDto, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/api/v1/posts/" + postId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                headers,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") String idString
    ) {
        try {
            long postId = Long.valueOf(idString);
            postService.delete(postId);

        } catch (PostNotFoundException e) {
            return new ResponseEntity<Object>(
                    new OperationStatusDto(HttpStatus.NOT_FOUND, "post not found"),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.OK, "success"),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/user/{id}", method = GET)
    public ResponseEntity<List<PostInfoDto>> showByUser(
            @PathVariable("id") String userIdString,
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString
    ) {
        long userId = Long.valueOf(userIdString);
        int start = Integer.valueOf(startString);
        int limit = Integer.valueOf(limitString);

        return new ResponseEntity<>(
                postService.findByUser(userId, start, limit), HttpStatus.OK);
    }
}
