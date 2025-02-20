package com.codedifferently.tsm.domain.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceCreationException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

@Controller
@RestController

@CrossOrigin
@RequestMapping("/api/v1/timesheet")
public class TimesheetController {
    //BASIC CRUD

    //create- need auth, throws permdenied, res create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody TimesheetEntity TimesheetEntity){
        //user creates their own timesheet

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    //read - get
    //get all- permdenied
    @GetMapping()
    public ResponseEntity<List<TimesheetEntity>> all(){
        //user = only view their user timesheets
        //supervisors = view all timesheets in worksite_id
        //coordinator = ?

    }

    //get by id- throws cant find
    @GetMapping("/{id}")
    public ResponseEntity<TimesheetEntity> timesheet(@PathVariable Integer id){
        //TODO: do we want to make 2 different methods? One for user parsing user timesheets vs
        // supervisor parsing employee timesheets?


        //same as above, just one singular 
    }


    //update- needs auth, throws cant find, permdenied, res create
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id){
        return new ResponseEntity<>("Successfully updated timesheet", HttpStatus.OK);
    }

    //delete- needs auth, throws cant find, perdenied
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id){
        return new ResponseEntity<>("Successfully deleted timesheet", HttpStatus.OK);
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
