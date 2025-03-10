package com.example.demo.event;

import com.example.demo.model.Status;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

public class BookingStatusEvent extends ApplicationEvent {
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status bookingStatus;

    public BookingStatusEvent (Object source, Long roomId, LocalDate startDate, LocalDate endDate, Status bookingStatus) {
        super(source);
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Status bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
