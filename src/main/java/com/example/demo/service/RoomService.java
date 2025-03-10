package com.example.demo.service;

import com.example.demo.dto.RoomDto;
import com.example.demo.event.RoomAddedEvent;
import com.example.demo.event.RoomDeletedEvent;
import com.example.demo.event.RoomUpdatedEvent;
import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RoomService(RoomRepository roomRepository, ApplicationEventPublisher eventPublisher) {
        this.roomRepository = roomRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Room> allRooms() {
        List<Room> rooms = new ArrayList<>();
        roomRepository.findAll().forEach(rooms::add);
        return rooms;
    }

    public Room getRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    public Room createRoom(RoomDto input) {
        Room room = new Room(input.getName(), input.getCapacity(), input.getTotal_count(), input.getBase_price());
        Room savedRoom = roomRepository.save(room);

        eventPublisher.publishEvent(new RoomAddedEvent(this, savedRoom.getId(), savedRoom.getTotal_count()));
        return savedRoom;
    }

    public Room updateRoom(Long id, RoomDto input) {
        Room room = new Room(input.getName(), input.getCapacity(), input.getTotal_count(), input.getBase_price());
        room.setId(id);
        Room updatedRoom = roomRepository.save(room);

        eventPublisher.publishEvent(new RoomUpdatedEvent(this, updatedRoom.getId(), updatedRoom.getTotal_count()));
        return updatedRoom;
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
        // Setting to 0 instead of Deleting
        // TODO: Handling edge cases for deletion and changing availability table depending on case
        eventPublisher.publishEvent(new RoomDeletedEvent(this, id));
    }
}
