package com.hotel.repository;

import com.hotel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * FROM test.bookings", nativeQuery = true)
    List<Booking> getFullBookingsList();

    List<Booking> findBookingByUserId(Long userId);

    List<Booking> findBookingByRoomId(Long roomId);

    @Query(value = "call test.validate_booking_date(:room_id, :start_date, :end_date)", nativeQuery = true)
    int validate(@Param("room_id") Long roomId, @Param("start_date") Date startDate, @Param("end_date") Date endDate);
}
