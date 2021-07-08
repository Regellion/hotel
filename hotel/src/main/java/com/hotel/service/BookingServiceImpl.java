package com.hotel.service;

import com.hotel.exception.BookingException;
import com.hotel.exception.RoomException;
import com.hotel.model.Booking;
import com.hotel.dto.BookingDto;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService,
                              RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    @Override
    public Booking saveBooking(BookingDto bookingDto) {
        Booking validateBooking = validateBooking(roomService.getRoomById(bookingDto.getRoomId()),
                userService.getUserById(bookingDto.getUserId()), bookingDto);
        return Optional.ofNullable(bookingRepository.saveBooking(validateBooking))
                .orElseThrow(() -> new BookingException("Booking failed"));
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.getAllBookings();
        if (bookings.size() == 0) {
            throw new BookingException("Bookings list is empty");
        }
        return bookings;
    }

    @Override
    public Booking getBookingById(Long id) {
        return Optional.ofNullable(bookingRepository.getBookingById(id)).orElseThrow(() -> new BookingException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingByUserId(Long id) {
        User user = userService.getUserById(id);
        List<Booking> bookings = bookingRepository.getBookingsByUserId(user.getId());
        if (bookings.size() == 0) {
            throw new BookingException("Bookings list for user " + id + " is empty");
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingByRoomId(Long id) {
        Room room = roomService.getRoomById(id);
        List<Booking> bookings = bookingRepository.getBookingsByRoomId(room.getId());
        if (bookings.size() == 0) {
            throw new BookingException("Bookings list for room " + id + " is empty");
        }
        return bookings;
    }

    @Override
    public void deleteAllBookings() {
        List<Booking> bookingList = bookingRepository.getAllBookings();
        if (bookingList.size() == 0) {
            throw new BookingException("Bookings list is empty");
        }
        bookingRepository.deleteAllBookings();
    }

    @Override
    public void deleteBookingById(Long id) {
        Booking tempBooking = Optional.ofNullable(bookingRepository.getBookingById(id))
                .orElseThrow(() -> new BookingException("Booking not found"));
        bookingRepository.deleteBookingById(tempBooking.getId());
    }

    private Booking validateBooking(Room room, User user, BookingDto bookingDto) {
        if (room.isUnderRenovation()) {
            throw new RoomException("The room is unavailable.");
        }
        List<Booking> bookings = bookingRepository.getBookingsByRoomId(bookingDto.getRoomId());
        Optional<Booking> roomIsBooked = bookings.stream()
                .filter(b -> (bookingDto.getStartDate().after(b.getStartDate()) && bookingDto.getStartDate().before(b.getEndDate())
                        || (bookingDto.getEndDate().after(b.getStartDate()) && bookingDto.getEndDate().before(b.getEndDate()))
                        || (b.getStartDate().after(bookingDto.getStartDate()) && b.getStartDate().before(bookingDto.getEndDate())))
                        || (b.getEndDate().after(bookingDto.getStartDate()) && b.getEndDate().before(bookingDto.getEndDate()))
                        || (b.getStartDate().getTime() == bookingDto.getStartDate().getTime() || b.getStartDate().getTime() == bookingDto.getEndDate().getTime())
                        || (b.getEndDate().getTime() == bookingDto.getStartDate().getTime() || b.getEndDate().getTime() == bookingDto.getEndDate().getTime()))
                .findFirst();
        if (roomIsBooked.isPresent()) {
            throw new BookingException("Incorrect booking dates.");
        }
        Booking booking = new Booking(room, user, bookingDto.getStartDate(), bookingDto.getEndDate());
        if (booking.getStartDate().after(booking.getEndDate())) {
            throw new BookingException("Start date is after end date.");
        }
        return booking;
    }
}
