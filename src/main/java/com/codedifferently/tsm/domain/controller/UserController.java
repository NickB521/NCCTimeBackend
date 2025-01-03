package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.service.impl.UserServiceImpl;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Tag(name = "Users", description = "Endpoints for viewing user information.")

@Controller
@RestController

@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Operation(summary = "Get All Users", description = "Fetches a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users."),
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> all() throws PermissionDeniedException {
        return ResponseEntity.ok(userService.getAllUsers(getAuthorities()));
    }


    @Operation(summary = "Get Authorized User", description = "Fetches a specific user by their authorization token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user."),
            @ApiResponse(responseCode = "400", description = "Invalid user email (Should never happen).")
    })
    @GetMapping("/self")
    public ResponseEntity<UserDto> self() throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getSelf(getEmail()));
    }


    @Operation(summary = "Get User by ID", description = "Fetches a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user."),
            @ApiResponse(responseCode = "400", description = "Invalid user id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> user(@PathVariable Integer id) throws PermissionDeniedException, ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUser(id, getAuthorities()));
    }


    @Operation(summary = "Get User by Email", description = "Fetches a specific user by their email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user."),
            @ApiResponse(responseCode = "400", description = "Invalid user email."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> email(@PathVariable String email) throws PermissionDeniedException, ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUser(email, getAuthorities()));
    }


    @Operation(summary = "Get Users by Worksite", description = "Fetches a list of users by their worksite.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users."),
            @ApiResponse(responseCode = "400", description = "Invalid worksite id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @GetMapping("/worksite/{id}")
    public ResponseEntity<List<UserDto>> worksiteUsers(@PathVariable Integer id) throws PermissionDeniedException, ResourceNotFoundException {
        return ResponseEntity.ok(userService.getWorksiteUsers(id, getAuthorities()));
    }


    private Collection<GrantedAuthority> getAuthorities() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(authority -> (GrantedAuthority) authority)
                .toList();
    }

    private String getEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDeniedException(PermissionDeniedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
