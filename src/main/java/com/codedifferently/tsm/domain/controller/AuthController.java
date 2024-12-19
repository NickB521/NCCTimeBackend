package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.AuthLoginDto;
import com.codedifferently.tsm.domain.model.dto.AuthRegisterDto;
import com.codedifferently.tsm.domain.model.dto.AuthTokenDto;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.repository.UserRepository;
import com.codedifferently.tsm.domain.service.impl.AuthServiceImpl;
import com.codedifferently.tsm.exception.ResourceCreationException;
import com.codedifferently.tsm.jwt.JwtTokenProvider;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration.")

@Controller
@RestController

@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserRepository userRepository,
                          AuthServiceImpl authService) {

        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authService = authService;
    }


    @Operation(summary = "Authenticate User", description = "Validates the user credentials and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful. JWT token returned."),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Invalid credentials.")
    })
    @PutMapping
    ResponseEntity<AuthTokenDto> authenticate(@RequestBody AuthLoginDto loginDto) {
        // Authenticate login credentials
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        // Return token if authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        AuthTokenDto dto = new AuthTokenDto(token, "Bearer");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @Operation(summary = "Register User", description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registration successful."),
            @ApiResponse(responseCode = "400", description = "Registration failed. Invalid input or user already exists.")
    })
    @PostMapping
    ResponseEntity<String> register(@RequestBody AuthRegisterDto registerDto) throws ResourceCreationException {
        UserEntity user = authService.createUser(registerDto);
        userRepository.save(user);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<String> handleResourceCreationException(ResourceCreationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
