package com.codedifferently.tsm.domain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.service.impl.AnnouncementsServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Announcements", description = "Endpoints for viewing announcement information.")

@Controller
@RestController

@CrossOrigin
@RequestMapping("/api/v1/announcements")
public class AnnouncementsController {
    //TODO: Get ALL announcements, get 1 announcement, add announcement

    private final AnnouncementsServiceImpl announcementsService;

    @Autowired
    public AnnouncementsController(AnnouncementsServiceImpl announcementsService) {
        this.announcementsService = announcementsService;
    }


    @Operation(summary = "Get All Announcements", description = "Fetches a list of all announcements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of announcements."),
    })
    @GetMapping
    public ResponseEntity<List<AnnouncementDto>> all() {
        return ResponseEntity.ok(announcementsService.getAllAnnouncements());
    }


    @Operation(summary = "Get Announcement by id", description = "Fetches an announcement by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the announcement."),
            @ApiResponse(responseCode = "400", description = "Invalid announcement id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDto> announcement(@PathVariable Integer id) throws ResourceNotFoundException {
        return ResponseEntity.ok(announcementsService.getAnnouncement(id));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    
}
