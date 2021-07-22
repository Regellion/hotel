package com.hotel.controller;

import com.hotel.dto.BookingDto;
import com.hotel.dto.RoomDto;
import com.hotel.dto.UserDto;
import com.hotel.service.BookingService;
import com.hotel.service.RoomService;
import com.hotel.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ui")
@AllArgsConstructor
public class UiController {

    private final BookingService bookingService;
    private final UserService userService;
    private final RoomService roomService;

    @RequestMapping("/bookings")
    public ModelAndView showAllBookings(ModelAndView modelAndView) {
        List<BookingDto> bookings = bookingService.getAllBookings();
        modelAndView.addObject("bookings", bookings).setViewName("Booking");
        return modelAndView;
    }

    @RequestMapping("/bookings/{id}")
    public ModelAndView showBookingById(ModelAndView modelAndView, @PathVariable long id) {
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(bookingService.getBookingById(id));
        modelAndView.addObject("bookings", bookings).setViewName("Booking");
        return modelAndView;
    }

    @RequestMapping("/bookings/users/{id}")
    public ModelAndView showBookingByUserId(ModelAndView modelAndView, @PathVariable long id) {
        List<BookingDto> bookings = bookingService.getBookingByUserId(id);
        modelAndView.addObject("bookings", bookings).setViewName("Booking");
        return modelAndView;
    }

    @RequestMapping("/bookings/rooms/{id}")
    public ModelAndView showBookingByRoomId(ModelAndView modelAndView, @PathVariable long id) {
        List<BookingDto> bookings = bookingService.getBookingByRoomId(id);
        modelAndView.addObject("bookings", bookings).setViewName("Booking");
        return modelAndView;
    }

    @RequestMapping("/users")
    public ModelAndView showAllUsers(ModelAndView modelAndView) {
        List<UserDto> users = userService.getAllUsers();
        modelAndView.addObject("users", users).setViewName("User");
        return modelAndView;
    }

    @RequestMapping("/users/{id}")
    public ModelAndView showUserById(ModelAndView modelAndView, @PathVariable long id) {
        List<UserDto> users = new ArrayList<>();
        users.add(userService.getUserById(id));
        modelAndView.addObject("users", users).setViewName("User");
        return modelAndView;
    }

    @RequestMapping("/rooms")
    public ModelAndView showAllRooms(ModelAndView modelAndView) {
        List<RoomDto> rooms = roomService.getAllRooms();
        modelAndView.addObject("rooms", rooms).setViewName("Room");
        return modelAndView;
    }

    @RequestMapping("/rooms/{id}")
    public ModelAndView showRoomById(ModelAndView modelAndView, @PathVariable long id) {
        List<RoomDto> rooms = new ArrayList<>();
        rooms.add(roomService.getRoomById(id));
        modelAndView.addObject("rooms", rooms).setViewName("Room");
        return modelAndView;
    }
}
