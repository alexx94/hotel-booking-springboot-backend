package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class AvailabilityId implements Serializable {
    private Long roomId;
    private LocalDate bookingDate;

    public AvailabilityId(Long room_id, LocalDate booking_date) {
        this.roomId = room_id;
        this.bookingDate = booking_date;
    }

    public AvailabilityId() {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AvailabilityId that = (AvailabilityId) obj;
        return Objects.equals(roomId, that.roomId) && Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, bookingDate);
    }
}
