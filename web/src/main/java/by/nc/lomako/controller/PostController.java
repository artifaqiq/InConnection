/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.exceptions.PostNotFoundException;
import by.nc.lomako.exceptions.UserNotFoundException;
import by.nc.lomako.security.UserDetailsImpl;
import by.nc.lomako.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    private final PostForCreateDto.DtoValidator postForCreateDtoValidator;

    @Autowired
    public PostController(PostService postService, PostForCreateDto.DtoValidator postForCreateDtoValidator) {
        this.postService = postService;
        this.postForCreateDtoValidator = postForCreateDtoValidator;
    }

    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<?> createPost(
            @RequestBody PostForCreateDto postDto,
            BindingResult bindingResult
    ) throws UserNotFoundException {

        postForCreateDtoValidator.validate(postDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<Object>(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getCode)
                            .collect(Collectors.joining("; ")),
                    HttpStatus.BAD_REQUEST
            );
        }

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long currentUserId = user.getUser().getId();

        long postId = postService.create(currentUserId, postDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/api/v1/posts/" + postId);

        return new ResponseEntity<>(
                new OperationStatusDto(HttpStatus.CREATED, "success"),
                headers,
                HttpStatus.CREATED
        );
    }

    @RequestMapping(value = "/{postId}", method = GET)
    public ResponseEntity<?> showPost(
            @PathVariable(value = "postId") String postIdString
    ) throws PostNotFoundException {

        long postId = Long.parseLong(postIdString);
        PostInfoDto postInfoDto = postService.findById(postId);

        return new ResponseEntity<>(
                postInfoDto,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{postId}", method = DELETE)
    public ResponseEntity<?> deletePost(
            @PathVariable(value = "postId") String postIdString
    ) throws PostNotFoundException {

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long currentUserId = user.getUser().getId();

        long postId = Long.parseLong(postIdString);

        PostInfoDto postInfoDto = postService.findById(postId);

        if (postInfoDto.getUserId() != currentUserId) {
            return new ResponseEntity<>(
                    new OperationStatusDto(
                            HttpStatus.FORBIDDEN,
                            "Access denied"
                    ),
                    HttpStatus.FORBIDDEN
            );
        }

        postService.deleteById(postId);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "user/{userId}/last/index", method = GET)
    public ResponseEntity<List<PostInfoDto>> getLastPostsByUser(
            @PathVariable("userId") String userIdString,
            @RequestParam("start") String startString,
            @RequestParam("limit") String limitString
    ) throws UserNotFoundException {

        long userId = Long.valueOf(userIdString);
        int start = Integer.valueOf(startString);
        int limit = Integer.valueOf(limitString);

        return new ResponseEntity<>(
                postService.findLastByUser(userId, start, limit), HttpStatus.OK);
    }

    @RequestMapping(value = "user/{userId}/last/count", method = GET)
    public ResponseEntity<?> getCountLastPostsByUser(
            @PathVariable("userId") String userIdString
    ) throws UserNotFoundException {
        long userId = Long.valueOf(userIdString);

        long countByUser = postService.countByUser(userId);

        return new ResponseEntity<>(
                countByUser,
                HttpStatus.OK
        );
    }
}
