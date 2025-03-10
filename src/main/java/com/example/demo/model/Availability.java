package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "availabilities")
@IdClass(AvailabilityId.class)
public class Availability {

    @Id
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Id
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "total_rooms", nullable = false)
    private int totalRooms;

    @Column(name = "booked_count", nullable = false)
    private int bookedCount;

    @Column(name = "available_count", insertable = false, updatable = false)
    private int availableCount;

    @Column(name = "closed", nullable = false)
    private boolean closed;

    public Availability(Long roomId, LocalDate bookingDate, int totalRooms, int bookedCount) {
        this.roomId = roomId;
        this.bookingDate = bookingDate;
        this.totalRooms = totalRooms;
        this.bookedCount = bookedCount;
        this.closed = true;
    }

    public Availability () {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
