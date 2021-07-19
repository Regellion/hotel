package com.hotel.service;

import com.hotel.dto.RoomDto;
import com.hotel.dto.UserDto;
import com.hotel.exception.BookingException;
import com.hotel.exception.RoomException;
import com.hotel.mapper.BookingMapper;
import com.hotel.dto.BookingDto;
import com.hotel.mapper.RoomMapper;
import com.hotel.mapper.UserMapper;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        Booking validateBooking = validateBooking(roomMapper.toEntity(roomService.getRoomById(bookingDto.getRoom().getId())),
                userMapper.toEntity(userService.getUserById(bookingDto.getUser().getId())), bookingDto);
        return bookingMapper.toDto(Optional.ofNullable(bookingRepository.saveBooking(validateBooking))
                .orElseThrow(() -> new BookingException("Booking failed")));
    }

    public BookingDto saveBooking(Long userId, BookingDto bookingDto) {
        bookingDto.setUser(userService.getUserById(userId));
        Booking validateBooking = validateBooking(roomMapper.toEntity(roomService.getRoomById(bookingDto.getRoom().getId())),
                userMapper.toEntity(userService.getUserById(bookingDto.getUser().getId())), bookingDto);
        return bookingMapper.toDto(Optional.ofNullable(bookingRepository.saveBooking(validateBooking))
                .orElseThrow(() -> new BookingException("Booking failed")));
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.getAllBookings().stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getFullBookingsList() {
        return bookingRepository.getFullBookingsList().stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingMapper.toDto(
                Optional.ofNullable(bookingRepository.getBookingById(id)).orElseThrow(() -> new BookingException("Booking not found")));
    }

    @Override
    public List<BookingDto> getBookingByUserId(Long id) {
        UserDto user = userService.getUserById(id);
        return bookingRepository.getBookingsByUserId(user.getId()).stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingByRoomId(Long id) {
        RoomDto room = roomService.getRoomById(id);
        return bookingRepository.getBookingsByRoomId(room.getId()).stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAllBookings() {
        bookingRepository.deleteAllBookings();
    }

    @Override
    public void deleteBookingById(Long id) {
        BookingDto tempBooking = bookingMapper.toDto(Optional.ofNullable(bookingRepository.getBookingById(id))
                .orElseThrow(() -> new BookingException("Booking not found")));
        bookingRepository.deleteBookingById(tempBooking.getId());
    }

    public void deleteBookingById(Long id, Long userId) {
        BookingDto bookingDto = getBookingByUserId(userId).stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookingException("Booking not found"));
        bookingRepository.deleteBookingById(bookingDto.getId());
    }

    private Booking validateBooking(Room room, User user, BookingDto bookingDto) {
        if (room.getUnderRenovation()) {
            throw new RoomException("The room is unavailable.");
        }
        List<BookingDto> bookings = bookingRepository.getBookingsByRoomId(bookingDto.getRoom().getId()).stream().map(bookingMapper::toDto).collect(Collectors.toList());
        Optional<BookingDto> roomIsBooked = bookings.stream()
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
