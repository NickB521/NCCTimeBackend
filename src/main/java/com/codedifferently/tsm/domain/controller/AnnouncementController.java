package com.codedifferently.tsm.domain.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.dto.CreateAnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.repository.AnnouncementRepository;
import com.codedifferently.tsm.domain.service.impl.AnnouncementServiceImpl;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceCreationException;
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
public class AnnouncementController {

    private final AnnouncementServiceImpl announcementsService;
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementController(AnnouncementServiceImpl announcementsService, AnnouncementRepository announcementRepository) {
        this.announcementsService = announcementsService;
        this.announcementRepository = announcementRepository;
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


    @Operation(summary = "Delete Announcement by id", description = "Deletes an announcement by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the announcement."),
            @ApiResponse(responseCode = "400", description = "Invalid announcement id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) throws ResourceNotFoundException, PermissionDeniedException {
        announcementsService.deleteAnnouncement(id, getAuthorities());
        return new ResponseEntity<>("Successfully deleted announcement", HttpStatus.OK);
    }


    @Operation(summary = "Update Announcement by id", description = "Updates an announcement by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the announcement."),
            @ApiResponse(responseCode = "400", description = "Invalid announcement id."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody CreateAnnouncementDto createAnnouncementDto) throws ResourceCreationException, PermissionDeniedException {
        announcementsService.updateAnnouncement(id, createAnnouncementDto, getAuthorities());
        return new ResponseEntity<>("Successfully updated announcement", HttpStatus.OK);
    }


    @Operation(summary = "Create an announcement", description = "Creates an announcement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the announcement."),
            @ApiResponse(responseCode = "400", description = "Creation failed. Invalid input."),
            @ApiResponse(responseCode = "403", description = "Permission denied."),
    })
    @PostMapping("/create")
    public ResponseEntity<String> createAnnouncement(@RequestBody CreateAnnouncementDto createAnnouncementDto) throws ResourceNotFoundException, ResourceCreationException, PermissionDeniedException {
        AnnouncementEntity announcement = announcementsService.createAnnouncement(createAnnouncementDto, getAuthorities());
        announcementRepository.save(announcement);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
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


    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<String> handleResourceCreationException(ResourceCreationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDeniedException(PermissionDeniedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
