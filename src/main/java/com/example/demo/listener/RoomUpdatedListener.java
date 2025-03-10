package com.example.demo.listener;

import com.example.demo.event.RoomAddedEvent;
import com.example.demo.event.RoomUpdatedEvent;
import com.example.demo.service.AvailabilityService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RoomUpdatedListener implements ApplicationListener<RoomUpdatedEvent> {
    private final AvailabilityService availabilityService;

    public RoomUpdatedListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(RoomUpdatedEvent event) {
        Long roomId = event.getRoomId();
        int totalRooms = event.getTotalRooms();
        System.out.println("Updating availability for room id: " + roomId + " with new total rooms: " + totalRooms);
        availabilityService.updateTotalRooms(roomId, totalRooms);
    }
}
