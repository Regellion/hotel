package com.hotel.controller;

import com.hotel.dto.BookingDto;
import com.hotel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public List<BookingDto> showAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/{id}")
    public BookingDto getBooking(@PathVariable long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/bookings/users/{id}")
    public List<BookingDto> getBookingsByUserId(@PathVariable long id) {
        return bookingService.getBookingByUserId(id);
    }

    @GetMapping("/bookings/rooms/{id}")
    public List<BookingDto> getBookingsByRoomId(@PathVariable long id) {
        return bookingService.getBookingByRoomId(id);
    }

    @PostMapping("/bookings")
    public BookingDto addBooking(@RequestBody BookingDto booking) {
        bookingService.saveBooking(booking);
        return booking;
    }

    @DeleteMapping("/bookings/{id}")
    public String deleteById(@PathVariable long id) {
        bookingService.deleteBookingById(id);
        return "Booking with ID = " + id + " was deleted";
    }

    @DeleteMapping("/bookings")
    public String deleteAllBookings() {
        bookingService.deleteAllBookings();
        return "All bookings was deleted";
    }
}
