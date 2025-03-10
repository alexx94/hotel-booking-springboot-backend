package com.example.demo.repository;

import com.example.demo.model.Availability;
import com.example.demo.model.AvailabilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {
    List<Availability> findByRoomIdAndBookingDateBetween(Long room_id, LocalDate startDate, LocalDate endDate);
    List<Availability> findByRoomId(Long room_id);
    void deleteByBookingDate(LocalDate booking_date);
    void deleteByRoomIdAndBookingDate(Long room_id, LocalDate booking_date);
}
