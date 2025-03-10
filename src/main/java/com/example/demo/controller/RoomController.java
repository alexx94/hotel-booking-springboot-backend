package com.example.demo.controller;

import com.example.demo.dto.RoomDto;
import com.example.demo.model.Room;
import com.example.demo.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rooms")
@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // TODO: Error class - ControllerAdvice class
    @GetMapping("/")
    public ResponseEntity<List<Room>> allRooms() {
        List<Room> rooms = roomService.allRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoom(id);
        return ResponseEntity.ok(room);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Room> createRoom(@RequestBody RoomDto input) {
        Room room = roomService.createRoom(input);
        return ResponseEntity.ok(room);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody RoomDto input) {
        Room updatedRoom = roomService.updateRoom(id, input);
        return ResponseEntity.ok(updatedRoom);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully.");
    }

}
