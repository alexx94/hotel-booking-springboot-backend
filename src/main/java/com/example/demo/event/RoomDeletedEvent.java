package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

public class RoomDeletedEvent extends ApplicationEvent {
    private Long roomId;

    public RoomDeletedEvent(Object source, Long roomId) {
        super(source);
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }
}
