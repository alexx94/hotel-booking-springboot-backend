package com.example.demo.listener;

import com.example.demo.event.BookingStatusEvent;
import com.example.demo.model.Availability;
import com.example.demo.model.Status;
import com.example.demo.service.AvailabilityService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookingStatusListener implements ApplicationListener<BookingStatusEvent> {
    private final AvailabilityService availabilityService;

    public BookingStatusListener (AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(BookingStatusEvent event) {
        Long roomId = event.getRoomId();
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();

        // find in repo after room id and dates in between start and end date
        // for each one of those, if status is COFNIRMED -> + 1, else if CANCELLED -> - 1 for booking_count

        if (event.getBookingStatus() == Status.CONFIRMED) {
            availabilityService.updateAfterStatusChange(roomId, startDate, endDate, 1);
        }
        else if (event.getBookingStatus() == Status.CANCELLED) {
            availabilityService.updateAfterStatusChange(roomId, startDate, endDate, -1);
        }
    }
}
