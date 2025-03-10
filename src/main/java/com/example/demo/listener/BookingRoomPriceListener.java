package com.example.demo.listener;

import com.example.demo.event.BookingCreationEvent;
import com.example.demo.service.RoomService;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(3)
public class BookingRoomPriceListener implements ApplicationListener<BookingCreationEvent> {
    private final RoomService roomService;

    public BookingRoomPriceListener(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void onApplicationEvent(BookingCreationEvent event) {
        Long roomId = event.getRoomId();
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();
        int nights = (int) event.getNightsCnt();
        int oldPrice = event.getTotalPrice();
        int roomPrice = roomService.getRoom(roomId).getBase_price();

        event.setTotalPrice(oldPrice + nights * roomPrice);
    }
}
