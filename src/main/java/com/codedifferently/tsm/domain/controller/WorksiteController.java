package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.service.impl.WorksiteServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@CrossOrigin
@RequestMapping("/api/v1/worksites")
public class WorksiteController {

    private final WorksiteServiceImpl worksiteService;

    @Autowired
    public WorksiteController(WorksiteServiceImpl worksiteService) {
        this.worksiteService = worksiteService;
    }

    @GetMapping
    public ResponseEntity<List<WorksiteEntity>> all() {return ResponseEntity.ok(worksiteService.getAllWorksites());}

    @GetMapping("/{id}")
    public ResponseEntity<WorksiteEntity> worksite(@PathVariable Integer id) {return ResponseEntity.ok(worksiteService.getWorksite(id));}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
