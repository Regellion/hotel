package com.hotel.controller;

import com.hotel.configuration.security.JwtUser;
import com.hotel.dto.BookingDto;
import com.hotel.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/admin/bookings")
    public List<BookingDto> showAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/admin/bookings/full")
    public List<BookingDto> showFullBookingsList() {
        return bookingService.getFullBookingsList();
    }

    @GetMapping("/admin/bookings/{id}")
    public BookingDto getBooking(@PathVariable long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/bookings")
    public List<BookingDto> getBooking() {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return getBookingsByUserId(currentUserId);
    }

    @GetMapping("/admin/bookings/users/{id}")
    public List<BookingDto> getBookingsByUserId(@PathVariable long id) {
        return bookingService.getBookingByUserId(id);
    }

    @GetMapping("/admin/bookings/rooms/{id}")
    public List<BookingDto> getBookingsByRoomId(@PathVariable long id) {
        return bookingService.getBookingByRoomId(id);
    }

    @PostMapping("/admin/bookings")
    public BookingDto addBooking(@RequestBody BookingDto booking) {
        bookingService.saveBooking(booking);
        return booking;
    }

    @PostMapping("/bookings")
    public BookingDto addBookingForCurrentUser(@RequestBody BookingDto booking) {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        bookingService.saveBooking(currentUserId, booking);
        return booking;
    }

    @DeleteMapping("/admin/bookings/{id}")
    public String deleteById(@PathVariable long id) {
        bookingService.deleteBookingById(id);
        return "Booking with ID = " + id + " was deleted";
    }

    @DeleteMapping("/bookings/{id}")
    public String deleteByIdFromCurrentUser(@PathVariable long id) {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        bookingService.deleteBookingById(id, currentUserId);
        return "Booking with ID = " + id + " was deleted";
    }

    @DeleteMapping("/admin/bookings")
    public String deleteAllBookings() {
        bookingService.deleteAllBookings();
        return "All bookings was deleted";
    }

    @GetMapping("/bookings/receipt/{fileName}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable String fileName) {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        InputStreamResource resource = bookingService.downloadReceipt(currentUserId, fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
