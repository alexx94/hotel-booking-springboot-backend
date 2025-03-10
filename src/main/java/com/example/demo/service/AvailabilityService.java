package com.example.demo.service;

import com.example.demo.model.Availability;
import com.example.demo.repository.AvailabilityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public List<Availability> allAvailabilities() {
        List<Availability> availabilities = new ArrayList<>();
        availabilityRepository.findAll().forEach(availabilities::add);
        return availabilities;
    }

    public List<Availability> getAvailabilitiesByRoomId(Long roomId) {
        return availabilityRepository.findByRoomId(roomId);
    }

    public List<LocalDate> getAvailableRoomsById(Long roomId) {
        List<LocalDate> availableDates = new ArrayList<>();
        List<Availability> availabilities = availabilityRepository.findByRoomId(roomId);

        for (Availability availability : availabilities) {
            if (!availability.isClosed() || availability.getAvailableCount() > 0) {
                availableDates.add(availability.getBookingDate());
            }
        }

        return availableDates;
    }

    public List<Availability> getRoomBetweenDates(Long roomId, LocalDate startDate, LocalDate endDate) {
        return availabilityRepository.findByRoomIdAndBookingDateBetween(roomId, startDate, endDate);
    }

    public boolean isRoomAvailableBetweenDates(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<Availability> availabilities = availabilityRepository.findByRoomIdAndBookingDateBetween(roomId, startDate, endDate);
        for (Availability availability : availabilities) {
            if (availability.isClosed() || availability.getAvailableCount() == 0) return false;
        }

        return true;
    }

    // Change the isClosed value within of a room type between a certain date range
    public void changeAvailabilityStatus(Long roomId, LocalDate startDate, LocalDate endDate, boolean isClosed) {
        List<Availability> availabilities = availabilityRepository
                .findByRoomIdAndBookingDateBetween(roomId, startDate, endDate);

        availabilities.forEach(availability -> availability.setClosed(isClosed));
        availabilityRepository.saveAll(availabilities);
    }

    /*
    *    Update total_rooms in case of Rooms Table update
    *    If new rooms total will be < booked rooms, then we will mark available as 0 to still honor the already
    *    booked rooms, unless the admin also cancels their booking to solve this issue
    */
    @Transactional
    public void updateTotalRooms(Long roomId, int newTotalRooms) {
        List<Availability> availabilities = availabilityRepository.findByRoomId(roomId);
        for (Availability availability : availabilities) {
            availability.setTotalRooms(newTotalRooms);
        }

        availabilityRepository.saveAll(availabilities);
    }

    @Transactional
    public void updateAfterStatusChange(Long roomId, LocalDate startDate, LocalDate endDate, int cnt) {
        List<Availability> availabilities = availabilityRepository.findByRoomIdAndBookingDateBetween(roomId, startDate, endDate);
        for (Availability availability : availabilities) {
            int currentCount = availability.getBookedCount();
            int updatedCount = Math.max(currentCount + cnt, 0);
            availability.setBookedCount(updatedCount);
        }

        if (!availabilities.isEmpty()) {
            availabilityRepository.saveAll(availabilities);
        }
    }

    @Transactional
    public void deleteById(Long roomId) {
        /*
         if no room was booked, delete that row, otherwise just set total_rooms value to 0
         so that you can't book further for that room, but visualize and honor any requests made,
         unless you specifically cancel the bookings and then try again deleting that entry
         */
        List<Availability> availabilities = availabilityRepository.findByRoomId(roomId);
        List<Availability> toDelete = new ArrayList<>();
        List<Availability> toUpdate = new ArrayList<>();

        for (Availability availability : availabilities) {
            if (availability.getBookedCount() == 0) {
                toDelete.add(availability);
            }
            else {
                availability.setTotalRooms(0);
                toUpdate.add(availability);
            }
        }

        if (!toDelete.isEmpty()) {
            availabilityRepository.deleteAll(toDelete);
        }

        if (!toUpdate.isEmpty()) {
            availabilityRepository.saveAll(toUpdate);
        }
    }



    /*
     *  Initialize availability for a new room type
     *  Method should be called after a new Room is added into Rooms Table (a new room type)
     */

    @Transactional
    public void initializeAvailabilityForRoom(Long roomId, int totalRooms) {
        List<Availability> availabilities = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(1);

        for (int i = 0; i < 365; i++) {
            availabilities.add(new Availability(roomId, startDate.plusDays(i), totalRooms, 0));
        }

        availabilityRepository.saveAll(availabilities);
    }


    // TODO: Add automatic daily newDate insertion and oldDate deletion at 00:00 with Synchronized
}
