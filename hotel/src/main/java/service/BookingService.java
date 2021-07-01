package service;

import dto.BookingDto;
import model.Booking;

import java.util.List;

public interface BookingService {

    Booking saveBooking(BookingDto bookingDto);

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    List<Booking> getBookingByUserId(Long id);

    List<Booking> getBookingByRoomId(Long id);

    void deleteAllBookings();

    void deleteBookingById(Long id);
}
