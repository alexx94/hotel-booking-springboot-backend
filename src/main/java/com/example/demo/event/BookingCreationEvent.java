package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingCreationEvent extends ApplicationEvent {
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isAvailable;
    private int totalPrice;
    private long totalNights;
    private long nightsCnt;

    public BookingCreationEvent(Object source, Long roomId, LocalDate startDate, LocalDate endDate) {
        super(source);
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalNights = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
        this.nightsCnt = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNights() {
        return (int) totalNights;
    }

    public long getNightsCnt() {
        return nightsCnt;
    }

    public void setNightsCnt(long nightsCnt) {
        this.nightsCnt = nightsCnt;
    }
}
