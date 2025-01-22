package com.codedifferently.tsm.domain.controller;

import com.codedifferently.tsm.domain.model.dto.HolidayDto;
import com.codedifferently.tsm.domain.service.impl.HolidayServiceImpl;
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

@Tag(name = "Holidays", description = "Endpoints for managing holiday information.")

@Controller
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
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of holidays."),
    })
    @GetMapping
    public ResponseEntity<List<HolidayDto>> all() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }

    @Operation(summary = "Get Holiday by ID", description = "Fetches a specific holiday by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the holiday."),
            @ApiResponse(responseCode = "400", description = "Invalid holiday ID."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<HolidayDto> holiday(@PathVariable Integer id) throws ResourceNotFoundException {
        return ResponseEntity.ok(holidayService.getHoliday(id));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}