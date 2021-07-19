package com.hotel.repository;

import com.hotel.model.Booking;

import java.util.List;

public interface BookingRepository {

    Booking saveBooking(Booking booking);

    Booking getBookingById(Long id);

    List<Booking> getBookingsByUserId(Long id);

    List<Booking> getBookingsByRoomId(Long id);

    List<Booking> getAllBookings();

    List<Booking> getFullBookingsList();

    void deleteBookingById(Long id);

    void deleteAllBookings();
}
