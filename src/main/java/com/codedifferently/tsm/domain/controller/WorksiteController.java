package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
import com.codedifferently.tsm.domain.service.impl.WorksiteServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Worksites", description = "Endpoints for viewing worksite information.")

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


    @Operation(summary = "Get All Worksites", description = "Fetches a list of all worksites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of worksites."),
    })
    @GetMapping
    public ResponseEntity<List<WorksiteDto>> all() {
        return ResponseEntity.ok(worksiteService.getAllWorksites());
    }


    @Operation(summary = "Get Worksite by id", description = "Fetches a worksite by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the worksite."),
            @ApiResponse(responseCode = "400", description = "Invalid worksite id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorksiteDto> worksite(@PathVariable Integer id) throws ResourceNotFoundException {
        return ResponseEntity.ok(worksiteService.getWorksite(id));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
