package repository;

import model.Booking;

import java.util.List;

public interface BookingRepository {

    Booking saveBooking(Booking booking);

    Booking getBookingById(Long id);

    List<Booking> getBookingsByUserId(Long id);

    List<Booking> getBookingsByRoomId(Long id);

    List<Booking> getAllBookings();

    void deleteBookingById(Long id);

    void deleteAllBookings();
}
