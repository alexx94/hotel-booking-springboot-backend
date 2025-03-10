package com.example.demo.dto;

import java.time.LocalDate;

public class AvailabilityStatusDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isClosed;

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

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        this.isClosed = closed;
    }
}
