/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.controller.admin;

import by.nc.lomako.dto.OperationStatusDto;
import by.nc.lomako.dto.post.PostForCreateDto;
import by.nc.lomako.dto.post.PostForUpdateDto;
import by.nc.lomako.dto.post.PostInfoDto;
import by.nc.lomako.services.PostService;
import by.nc.lomako.services.exceptions.PostNotFoundException;
import by.nc.lomako.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController("adminPostController")
@RequestMapping("/api/v1/admin/posts")
public class PostController {

    private final PostService postService;

    private final PostForCreateDto.DtoValidator postForCreateDtoValidator;

    private final PostForUpdateDto.DtoValidator postForUpdateDtoValidator;

    @Autowired
    public PostController(PostService postService, PostForCreateDto.DtoValidator postForCreateDtoValidator,
                          PostForUpdateDto.DtoValidator postForUpdateDtoValidator) {
        this.postService = postService;
        this.postForCreateDtoValidator = postForCreateDtoValidator;
        this.postForUpdateDtoValidator = postForUpdateDtoValidator;
    }

    @RequestMapping(value = "/user/{userId}/create", method = POST)
    public ResponseEntity<?> createPost(
            @PathVariable("userId") String userIdString,
            @RequestBody PostForCreateDto postForCreateDto,
            BindingResult bindingResult
    ) throws UserNotFoundException {

        long userId = Long.parseLong(userIdString);

        postForCreateDtoValidator.validate(postForCreateDto, bindingResult);
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

        postService.create(userId, postForCreateDto);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.CREATED,
                        "success"
                ),
                HttpStatus.CREATED
        );
    }

    @RequestMapping(value = "/{postId}/update", method = PUT)
    public ResponseEntity<?> updatePost(
            @PathVariable("postId") String postIdString,
            @RequestBody PostForUpdateDto postForUpdateDto,
            BindingResult bindingResult
    ) throws PostNotFoundException, UserNotFoundException {

        long postId = Long.parseLong(postIdString);

        postForUpdateDtoValidator.validate(postForUpdateDto, bindingResult);
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

        postService.update(postId, postForUpdateDto);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{postId}", method = DELETE)
    public ResponseEntity<?> deletePost(
            @PathVariable("postId") String postIdString
    ) throws PostNotFoundException {
        long postId = Long.parseLong(postIdString);

        postService.deleteById(postId);

        return new ResponseEntity<>(
                new OperationStatusDto(
                        HttpStatus.OK,
                        "success"
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{postId}", method = GET)
    public ResponseEntity<?> showPost(
            @PathVariable("postId") String postIdString
    ) throws PostNotFoundException {

        long postId = Long.parseLong(postIdString);

        PostInfoDto postInfoDto = postService.findById(postId);

        return new ResponseEntity<>(
                postInfoDto,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/index", method = GET)
    public ResponseEntity<?> showAll() {
        List<PostInfoDto> posts = postService.findAll();

        return new ResponseEntity<>(
                posts,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/count", method = GET)
    public ResponseEntity<?> count() {
        long count = postService.count();

        return new ResponseEntity<>(
                count,
                HttpStatus.OK
        );
    }
}
