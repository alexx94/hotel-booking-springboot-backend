package com.example.demo.service;

import com.example.demo.dto.BookingDto;
import com.example.demo.event.BookingCreationEvent;
import com.example.demo.event.BookingStatusEvent;
import com.example.demo.model.Booking;
import com.example.demo.model.Role;
import com.example.demo.model.Status;
import com.example.demo.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BookingService(BookingRepository bookingRepository, ApplicationEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Booking> allBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookingRepository.findAll().forEach(bookings::add);
        return bookings;
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public List<Booking> getUserBookings(Long user_id) {
        return bookingRepository.findByUserId(user_id);
    }

    @Transactional
    public void confirmBooking(Long id, String statusMessage) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Booking not founds with id: " + id));

        if (booking.getStatus() == Status.CONFIRMED) {
            throw new IllegalStateException("Booking is already confirmed.");
        }

        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking has been cancelled.");
        }

        booking.setStatus(Status.CONFIRMED);
        booking.setStatusMessage(statusMessage);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        BookingStatusEvent event = new BookingStatusEvent(this, booking.getRoomId(), booking.getStartDate(), booking.getEndDate().minusDays(1), booking.getStatus());
        eventPublisher.publishEvent(event);
    }

    // method can be used by both user and admin roles to cancel a booking before its fulfilled
    @Transactional
    public void cancelBooking(Long userId, Role userRole, Long bookingId, String statusMessage) {
        Booking booking;
        boolean isBookingStatusEvent;
        if (userRole == Role.ADMIN) {
            booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
        } else {
            booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
        }


        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled.");
        }

        /*
              if booking status == PENDING -> nu apelez eventul, ci doar modific statusul din Booking Table
              else if status == CONFIRMED -> apelez eventul, pt ca trebuie modifica si Availabilitiies Table
        */
        if (booking.getStatus() == Status.PENDING) {
            isBookingStatusEvent = false;
        } else {
            isBookingStatusEvent = true;
        }

        booking.setStatus(Status.CANCELLED);
        booking.setStatusMessage(statusMessage);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        if (isBookingStatusEvent) {
            BookingStatusEvent event = new BookingStatusEvent(this, booking.getRoomId(), booking.getStartDate(), booking.getEndDate().minusDays(1), booking.getStatus());
            eventPublisher.publishEvent(event);
        }
    }

    public String testCreateEvent(BookingDto input) {
        BookingCreationEvent event = new BookingCreationEvent(this, input.getRoomId(), input.getStartDate(), input.getEndDate().minusDays(1));
        eventPublisher.publishEvent(event);

        if (!event.isAvailable()) return "No availability between " + event.getStartDate() + " and " + event.getEndDate();

        return "Hotel availability between " + event.getStartDate() + " and " + event.getEndDate() + " is: " +
                event.isAvailable() + " with total price: " + event.getTotalPrice() + " , nights without special price: " + event.getNightsCnt();
    }


    public Booking createBooking(Long userId, BookingDto input) {
        if (input.getStartDate().isAfter(input.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        BookingCreationEvent event = new BookingCreationEvent(this, input.getRoomId(), input.getStartDate(), input.getEndDate().minusDays(1));
        eventPublisher.publishEvent(event);

        if (!event.isAvailable()) {
            throw new IllegalStateException("Room not available for the selected dates.");
        }

        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking(
                userId,
                input.getRoomId(),
                input.getStartDate(),
                input.getEndDate(),
                event.getTotalNights(),
                event.getTotalPrice(),
                now,
                now
                );

        return bookingRepository.save(booking);
    }

}
