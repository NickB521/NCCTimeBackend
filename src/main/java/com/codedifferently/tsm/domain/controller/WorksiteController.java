package com.codedifferently.tsm.domain.controller;
import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
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
    public ResponseEntity<List<WorksiteDto>> all() {
        List<WorksiteDto> worksites = worksiteService.getAllWorksites();
        return ResponseEntity.ok(worksites);
    }

    @GetMapping("/{worksite_id}")
    public ResponseEntity<WorksiteDto> worksite(@PathVariable Integer worksite_id) {
        WorksiteDto worksite = worksiteService.getWorksite(worksite_id);
        return ResponseEntity.ok(worksite);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
