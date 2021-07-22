package com.hotel.service;

import com.hotel.dto.BookingDto;

import java.util.List;

public interface BookingService {

    void saveBooking(BookingDto bookingDto);

    void saveBooking(Long userId, BookingDto bookingDto);

    List<BookingDto> getAllBookings();

    List<BookingDto> getFullBookingsList();

    BookingDto getBookingById(Long id);

    List<BookingDto> getBookingByUserId(Long id);

    List<BookingDto> getBookingByRoomId(Long id);

    void deleteAllBookings();

    void deleteBookingById(Long id);

    void deleteBookingById(Long id, Long userId);
}
