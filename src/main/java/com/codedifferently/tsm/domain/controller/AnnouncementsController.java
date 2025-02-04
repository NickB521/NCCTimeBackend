package com.codedifferently.tsm.domain.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.dto.CreateAnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.repository.AnnouncementRepository;
import com.codedifferently.tsm.domain.service.impl.AnnouncementsServiceImpl;
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
public class AnnouncementsController {

    private final AnnouncementsServiceImpl announcementsService;
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementsController(AnnouncementsServiceImpl announcementsService, AnnouncementRepository announcementRepository) {
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

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) throws ResourceNotFoundException, PermissionDeniedException {
        announcementsService.deleteAnnouncement(id, getAuthorities());
        return new ResponseEntity<>("Succesfully deleted annoucnement", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody CreateAnnouncementDto createAnnouncementDto) throws ResourceCreationException, PermissionDeniedException {
        AnnouncementEntity announcementEntity = announcementsService.updateAnnouncement(id, createAnnouncementDto, getAuthorities());
        return new ResponseEntity<>("Update complete", HttpStatus.OK);
    }

    @Operation(summary = "Create announcement w/ or w/o id", description = "Fetches an announcement by their id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the announcement."),
            @ApiResponse(responseCode = "400", description = "Invalid announcement id."),
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    
}
