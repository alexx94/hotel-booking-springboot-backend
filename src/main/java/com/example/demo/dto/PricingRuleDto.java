package com.example.demo.dto;

import java.time.LocalDate;

public class PricingRuleDto {
    private Long room_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private int special_price;

    public Long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Long room_id) {
        this.room_id = room_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(int special_price) {
        this.special_price = special_price;
    }
}
