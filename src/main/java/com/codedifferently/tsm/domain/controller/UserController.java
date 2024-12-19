package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.service.impl.UserServiceImpl;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<UserDto>> all() throws PermissionDeniedException {
        return ResponseEntity.ok(userService.getAllUsers(getAuthorities()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> user(@PathVariable Integer id) throws PermissionDeniedException, ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUser(id, getAuthorities()));
    }

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
