package com.codedifferently.tsm.domain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.codedifferently.tsm.domain.repository.UserRepository;

@Controller
@RestController

@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public Object getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }

}
