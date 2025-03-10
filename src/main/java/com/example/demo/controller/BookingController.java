package com.example.demo.controller;

import com.example.demo.dto.BookingDto;
import com.example.demo.model.Booking;
import com.example.demo.model.User;
import com.example.demo.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/booking")
@RestController
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/available")
    public ResponseEntity<String> checkBookingAvailability(@RequestBody BookingDto input) {
        String message = bookingService.testCreateEvent(input);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Booking>> getUserBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Booking> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/new")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDto input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Booking booking = bookingService.createBooking(user.getId(), input);
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<String> confirmBooking(@PathVariable Long bookingId,
                                                 @RequestBody(required = false) String message) {
        bookingService.confirmBooking(bookingId, message);
        return ResponseEntity.ok("Booking confirmed" + (message != null ? " with message: " + message : ""));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBookingAsUser(@PathVariable Long bookingId,
                                                 @RequestBody(required = false) String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        bookingService.cancelBooking(user.getId(), user.getRole(), bookingId, message);
        return ResponseEntity.ok("Booking cancelled" + (message != null ? " with message: " + message : ""));
    }
}
