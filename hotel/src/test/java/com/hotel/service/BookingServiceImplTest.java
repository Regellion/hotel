package com.hotel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.BookingDto;
import com.hotel.dto.BookingReceiptDto;
import com.hotel.exception.BookingException;
import com.hotel.exception.RoomException;
import com.hotel.exception.UserException;
import com.hotel.mapper.BookingMapper;
import com.hotel.mapper.RoomMapper;
import com.hotel.mapper.UserMapper;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.UserRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    BookingMapper bookingMapper;
    ModelMapper modelMapper;
    RoomMapper roomMapper;
    UserMapper userMapper;
    ObjectMapper objectMapper;
    KafkaTemplate<Long, BookingReceiptDto> kafkaTemplate;
    RestTemplate restTemplate;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserServiceImpl userService;
    @Mock
    RoomServiceImpl roomService;

    BookingServiceImpl bookingService;

    @Rule
    ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        roomMapper = new RoomMapper(modelMapper);
        userMapper = new UserMapper(modelMapper);
        bookingMapper = new BookingMapper(modelMapper);
        userService = new UserServiceImpl(userRepository, bookingRepository, userMapper, passwordEncoder);
        roomService = new RoomServiceImpl(roomRepository, roomMapper, bookingRepository);
        bookingService = new BookingServiceImpl(bookingMapper, objectMapper, roomMapper, userMapper,
                kafkaTemplate, restTemplate, bookingRepository, userService, roomService);
    }

    @Test
    void saveBookingValidate() {
        Room room = Room.builder().id(1L).underRenovation(false).price(1000).build();
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 100000000L);

        Booking booking = Booking.builder().id(1L).startDate(startDate).endDate(endDate).room(room).user(user).build();

        when(roomRepository.findById(booking.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(booking.getUser().getId())).thenReturn(Optional.of(user));
        Throwable throwable = assertThrows(NullPointerException.class, () -> bookingService.saveBooking(bookingMapper.toDto(booking)));
        assertNull(throwable.getMessage());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void saveBookingUnderRenovation() {
        Room room = Room.builder().id(1L).underRenovation(true).price(1000).build();
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 100000000L);

        Booking booking = Booking.builder().id(1L).startDate(startDate).endDate(endDate).room(room).user(user).build();

        when(roomRepository.findById(booking.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(booking.getUser().getId())).thenReturn(Optional.of(user));

        Throwable thrown = assertThrows(RoomException.class, () -> bookingService.saveBooking(bookingMapper.toDto(booking)));

        verify(bookingRepository, times(0)).save(any(Booking.class));


        assertNotNull(thrown.getMessage());
        assertEquals("The room is unavailable.", thrown.getMessage());
    }

    @Test
    void saveBookingIncorrectDates() {
        Room room = Room.builder().id(1L).underRenovation(false).price(1000).build();
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 100000000L);

        Booking booking = Booking.builder().id(1L).startDate(startDate).endDate(endDate).room(room).user(user).build();
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(roomRepository.findById(booking.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(booking.getUser().getId())).thenReturn(Optional.of(user));
        Throwable throwable = assertThrows(NullPointerException.class, ()-> bookingService.saveBooking(bookingMapper.toDto(booking)));
        assertNull(throwable.getMessage());
        verify(bookingRepository, times(1)).save(any(Booking.class));

        Booking booking2 = Booking.builder().id(2L).startDate(startDate).endDate(endDate).user(user).room(room).build();

        when(roomRepository.findById(booking2.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(booking2.getUser().getId())).thenReturn(Optional.of(user));
        when(bookingRepository.validate(booking2.getRoom().getId(), booking2.getStartDate(), booking2.getEndDate())).thenReturn(1);

        Throwable thrown = assertThrows(BookingException.class, () -> bookingService.saveBooking(bookingMapper.toDto(booking2)));
        assertNotNull(thrown.getMessage());
        assertEquals("Incorrect booking dates.", thrown.getMessage());
    }

    @Test
    void saveBookingStartAfterEnd() {
        Room room = Room.builder().id(1L).underRenovation(false).price(1000).build();
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 100000000L);

        Booking booking = Booking.builder().id(1L).startDate(endDate).endDate(startDate).room(room).user(user).build();

        when(roomRepository.findById(booking.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(booking.getUser().getId())).thenReturn(Optional.of(user));

        Throwable thrown = assertThrows(BookingException.class, () -> bookingService.saveBooking(bookingMapper.toDto(booking)));
        assertNotNull(thrown.getMessage());
        assertEquals("Start date is after end date.", thrown.getMessage());
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    void userSaveBooking() {
        Room room = Room.builder().id(1L).underRenovation(false).price(1000).build();
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 100000000L);

        Booking booking = Booking.builder().id(1L).startDate(startDate).endDate(endDate).room(room).user(user).build();

        when(roomRepository.findById(booking.getRoom().getId())).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Throwable throwable = assertThrows(NullPointerException.class, ()-> bookingService.saveBooking(1L, bookingMapper.toDto(booking)));
        assertNull(throwable.getMessage());
        assertEquals("testPass", booking.getUser().getPassword());

        Throwable thrown = assertThrows(UserException.class, () -> bookingService.saveBooking(2L, bookingMapper.toDto(booking)));

        assertNotNull(thrown.getMessage());
        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        Booking booking = Booking.builder().id(1L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).build();
        Booking booking2 = Booking.builder().id(2L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).build();
        Booking booking3 = Booking.builder().id(3L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).build();

        bookings.add(booking);
        bookings.add(booking2);
        bookings.add(booking3);

        when(bookingRepository.findAll()).thenReturn(bookings);
        List<BookingDto> bookingDtoList = bookingService.getAllBookings();
        assertEquals(3, bookingDtoList.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void getFullBookingsList() {
        List<Booking> bookings = new ArrayList<>();
        Booking booking = Booking.builder().id(1L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).deleteTime(new Date()).build();
        Booking booking2 = Booking.builder().id(2L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).deleteTime(new Date()).build();
        Booking booking3 = Booking.builder().id(3L).room(new Room()).user(new User()).startDate(new Date()).endDate(new Date()).build();

        bookings.add(booking);
        bookings.add(booking2);
        bookings.add(booking3);

        when(bookingRepository.findAll()).thenReturn(
                bookings.stream()
                        .filter(booking1 -> booking1.getDeleteTime() == null)
                        .collect(Collectors.toList()));
        List<BookingDto> bookingDtoList = bookingService.getAllBookings();
        assertEquals(1, bookingDtoList.size());
        verify(bookingRepository, times(1)).findAll();

        when(bookingRepository.getFullBookingsList()).thenReturn(
                bookings.stream()
                        .filter(booking1 -> booking1.getDeleteTime() != null)
                        .collect(Collectors.toList()));
        List<BookingDto> allBookingsDtoList = bookingService.getFullBookingsList();
        assertEquals(2, allBookingsDtoList.size());
        verify(bookingRepository, times(1)).getFullBookingsList();
    }

    @Test
    void getBookingById() {
        Booking booking = Booking.builder().id(1L).build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        BookingDto bookingDto = bookingService.getBookingById(1L);

        assertEquals(bookingDto, bookingMapper.toDto(booking));
        verify(bookingRepository, times(1)).findById(1L);

        Throwable thrown = assertThrows(BookingException.class, () -> bookingService.getBookingById(2L));

        assertNotNull(thrown.getMessage());
        assertEquals("Booking not found", thrown.getMessage());
        verify(bookingRepository, times(1)).findById(2L);
    }

    @Test
    void getBookingByUserId() {
        List<Booking> bookings = new ArrayList<>();
        User user = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();

        Booking booking = Booking.builder().id(1L).user(user).build();
        Booking booking2 = Booking.builder().id(2L).user(user2).build();
        Booking booking3 = Booking.builder().id(3L).user(user).build();

        bookings.add(booking);
        bookings.add(booking2);
        bookings.add(booking3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(bookingRepository.findBookingByUserId(1L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getUser().getId() == 1L).collect(Collectors.toList()));
        when(bookingRepository.findBookingByUserId(2L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getUser().getId() == 2L).collect(Collectors.toList()));

        List<BookingDto> bookingDtoListUser = bookingService.getBookingByUserId(1L);
        List<BookingDto> bookingDtoListUser2 = bookingService.getBookingByUserId(2L);

        assertEquals(2, bookingDtoListUser.size());
        assertEquals(1, bookingDtoListUser2.size());
        verify(bookingRepository, times(2)).findBookingByUserId(any());
    }

    @Test
    void getBookingByRoomId() {
        List<Booking> bookings = new ArrayList<>();
        Room room = Room.builder().id(1L).build();
        Room room2 = Room.builder().id(2L).build();
        Booking booking = Booking.builder().id(1L).room(room).build();
        Booking booking2 = Booking.builder().id(2L).room(room2).build();
        Booking booking3 = Booking.builder().id(3L).room(room).build();

        bookings.add(booking);
        bookings.add(booking2);
        bookings.add(booking3);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room2));
        when(bookingRepository.findBookingByRoomId(1L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getRoom().getId() == 1L).collect(Collectors.toList()));
        when(bookingRepository.findBookingByRoomId(2L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getRoom().getId() == 2L).collect(Collectors.toList()));

        List<BookingDto> bookingDtoListRoom = bookingService.getBookingByRoomId(1L);
        List<BookingDto> bookingDtoListRoom2 = bookingService.getBookingByRoomId(2L);

        assertEquals(2, bookingDtoListRoom.size());
        assertEquals(1, bookingDtoListRoom2.size());
        verify(bookingRepository, times(2)).findBookingByRoomId(any());
    }

    @Test
    void deleteAllBookings() {
        bookingService.deleteAllBookings();
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void deleteBookingById() {
        Booking booking = Booking.builder().id(1L).build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.deleteBookingById(1L);
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(booking);
        Throwable thrown = assertThrows(BookingException.class, () -> bookingService.deleteBookingById(2L));
        assertNotNull(thrown.getMessage());
        assertEquals("Booking not found", thrown.getMessage());
        verify(bookingRepository, times(1)).findById(2L);
    }

    @Test
    void deleteBookingByUserId() {
        Long userId = 1L;
        Long bookingId = 1L;
        List<Booking> bookings = new ArrayList<>();
        User user = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Booking booking = Booking.builder().id(1L).room(new Room()).startDate(new Date()).endDate(new Date()).user(user).build();
        Booking booking2 = Booking.builder().id(2L).room(new Room()).startDate(new Date()).endDate(new Date()).user(user2).build();
        Booking booking3 = Booking.builder().id(3L).room(new Room()).startDate(new Date()).endDate(new Date()).user(user).build();

        bookings.add(booking);
        bookings.add(booking2);
        bookings.add(booking3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(bookingRepository.findBookingByUserId(1L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getUser().getId() == 1L).collect(Collectors.toList()));
        when(bookingRepository.findBookingByUserId(2L))
                .thenReturn(bookings.stream().filter(booking1 -> booking1.getUser().getId() == 2L).collect(Collectors.toList()));

        List<BookingDto> bookingDtoListUser = bookingService.getBookingByUserId(userId);
        List<BookingDto> bookingDtoListUser2 = bookingService.getBookingByUserId(2L);

        bookingService.deleteBookingById(bookingId, userId);

        assertEquals(2, bookingDtoListUser.size());
        assertEquals(1, bookingDtoListUser2.size());
        verify(bookingRepository, times(2)).findBookingByUserId(userId);
        verify(bookingRepository, times(1)).save(any(Booking.class));

        Throwable thrown = assertThrows(BookingException.class, () -> bookingService.deleteBookingById(bookingId, 2L));

        assertNotNull(thrown.getMessage());
        assertEquals("Booking not found", thrown.getMessage());
    }
}