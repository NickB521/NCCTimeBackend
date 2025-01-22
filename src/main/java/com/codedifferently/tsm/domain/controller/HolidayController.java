package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.HolidayDto;
import com.codedifferently.tsm.domain.service.impl.HolidayServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Holidays", description = "Endpoints for managing holiday information.")
@RestController
@CrossOrigin
@RequestMapping("/api/v1/holidays")
public class HolidayController {
    private final HolidayServiceImpl holidayService;

    @Autowired
    public HolidayController(HolidayServiceImpl holidayService) {
        this.holidayService = holidayService;
    }

    @Operation(summary = "Get All Holidays", description = "Fetches a list of all holidays.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of holidays.")
    })
    @GetMapping
    public ResponseEntity<List<HolidayDto>> getAllHolidays() {
        List<HolidayDto> holidays = holidayService.getAllHolidays();
        return ResponseEntity.ok(holidays);
    }

    @Operation(summary = "Get Holiday by ID", description = "Fetches a specific holiday by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the holiday."),
            @ApiResponse(responseCode = "404", description = "Holiday not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HolidayDto> getHolidayById(@PathVariable Integer id) {
        try {
            HolidayDto holiday = holidayService.getHoliday(id);
            return ResponseEntity.ok(holiday);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}