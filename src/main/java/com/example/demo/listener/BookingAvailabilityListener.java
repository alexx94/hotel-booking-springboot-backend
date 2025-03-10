package com.example.demo.listener;

import com.example.demo.event.BookingCreationEvent;
import com.example.demo.service.AvailabilityService;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(1)
public class BookingAvailabilityListener implements ApplicationListener<BookingCreationEvent> {
    private final AvailabilityService availabilityService;

    public BookingAvailabilityListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(BookingCreationEvent event) {
        Long roomId = event.getRoomId();
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();

        if (availabilityService.isRoomAvailableBetweenDates(roomId, startDate, endDate)) {
            event.setAvailable(true);
        }
        else event.setAvailable(false);
    }

}
