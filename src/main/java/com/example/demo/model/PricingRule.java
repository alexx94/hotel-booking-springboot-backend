package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pricing_rules")
public class PricingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int special_price;

    public PricingRule(Long room_id, LocalDate start_date, LocalDate end_date, int special_price) {
        this.roomId = room_id;
        this.startDate = start_date;
        this.endDate = end_date;
        this.special_price = special_price;
    }

    public PricingRule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoom_id() {
        return roomId;
    }

    public void setRoom_id(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getStart_date() {
        return startDate;
    }

    public void setStart_date(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEnd_date() {
        return endDate;
    }

    public void setEnd_date(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(int special_price) {
        this.special_price = special_price;
    }
}
