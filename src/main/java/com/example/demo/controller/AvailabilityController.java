package com.example.demo.controller;

import com.example.demo.dto.AvailabilityStatusDto;
import com.example.demo.model.Availability;
import com.example.demo.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/availability")
@RestController
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController (AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Availability>> getAvailabilities() {
        List<Availability> availabilities = availabilityService.allAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/closed-dates/{id}")
    public ResponseEntity<List<LocalDate>> getClosedDates(@PathVariable Long id) {
        List<LocalDate> availableDates = availabilityService.getRoomClosedDates(id);
        return ResponseEntity.ok(availableDates);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fully-booked/{id}")
    public ResponseEntity<List<LocalDate>> getFullyBookedDates(@PathVariable Long id) {
        List<LocalDate> availableDates = availabilityService.getRoomFullyBookedDates(id);
        return ResponseEntity.ok(availableDates);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<String> updateAvailabilityStatus(@PathVariable Long id, @RequestBody AvailabilityStatusDto input) {
        availabilityService.changeAvailabilityStatus(id, input.getStartDate(), input.getEndDate(), input.isClosed());
        //String output = input.getStartDate().toString() + input.getEndDate().toString() + input.isClosed();
        System.out.println(input.isClosed());
        if (input.isClosed()) return ResponseEntity.ok("CLOSED - for Room Id: " + id + " between: " + input.getStartDate() + " and " + input.getEndDate());
        else return ResponseEntity.ok("OPEN - for Room Id: " + id + " between: " + input.getStartDate() + " and " + input.getEndDate());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LocalDate>> getAvailabilitiesByRoomId(@PathVariable Long id) {
        List<LocalDate> availableDates = availabilityService.getAvailableRoomsById(id);
        return ResponseEntity.ok(availableDates);
    }

    //TODO: adding different criteria for get requests (after id etc.)

}
