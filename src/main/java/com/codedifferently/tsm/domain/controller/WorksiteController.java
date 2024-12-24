package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.service.impl.WorksiteServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/worksites")
public class WorksiteController {
    private final WorksiteServiceImpl worksiteService;

    @Autowired
    public WorksiteController(WorksiteServiceImpl worksiteService) {this.worksiteService = worksiteService;}

    @GetMapping
    public ResponseEntity<List<UserDto>> all() {return ResponseEntity.ok(worksiteService.getAllWorksites());}

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> worksite(@PathVariable Integer id) {return ResponseEntity.ok(worksiteService.getWorksite(id));}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);}
}
