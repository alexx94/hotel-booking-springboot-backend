package com.example.demo.listener;

import com.example.demo.event.RoomDeletedEvent;
import com.example.demo.service.AvailabilityService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RoomDeletedListener implements ApplicationListener<RoomDeletedEvent> {
    private final AvailabilityService availabilityService;

    public RoomDeletedListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(RoomDeletedEvent event) {
        Long roomId = event.getRoomId();
        System.out.println("Deleting availability for room id: " + roomId);
        availabilityService.deleteById(roomId);
        // TODO: implement in AvailabilityService class the logic..
    }
}
