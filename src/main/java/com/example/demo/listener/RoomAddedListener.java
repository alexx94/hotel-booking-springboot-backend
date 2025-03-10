package com.example.demo.listener;

import com.example.demo.event.RoomAddedEvent;
import com.example.demo.service.AvailabilityService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RoomAddedListener implements ApplicationListener<RoomAddedEvent> {
    private final AvailabilityService availabilityService;

    public RoomAddedListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(RoomAddedEvent event) {
        Long roomId = event.getRoomId();
        int totalRooms = event.getTotalRooms();
        System.out.println("Initializing availability for room id: " + roomId);
        availabilityService.initializeAvailabilityForRoom(roomId, totalRooms);
    }
}
