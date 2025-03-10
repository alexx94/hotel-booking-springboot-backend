package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

public class RoomUpdatedEvent extends ApplicationEvent {
    private Long roomId;
    private int totalRooms;

    public RoomUpdatedEvent(Object source, Long roomId, int totalRooms) {
        super(source);
        this.roomId = roomId;
        this.totalRooms = totalRooms;
    }

    public Long getRoomId() {
        return this.roomId;
    }

    public int getTotalRooms() {
        return this.totalRooms;
    }
}
