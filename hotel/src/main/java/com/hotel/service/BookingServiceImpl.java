package com.hotel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.*;
import com.hotel.exception.BookingException;
import com.hotel.exception.RoomException;
import com.hotel.mapper.BookingMapper;
import com.hotel.mapper.RoomMapper;
import com.hotel.mapper.UserMapper;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.io.InputStreamResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final String URL = "http://localhost:8081/api/loadfile/";

    private final BookingMapper bookingMapper;
    private final ObjectMapper objectMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;
    private final KafkaTemplate<Long, BookingReceiptDto> kafkaTemplate;
    private final RestTemplate restTemplate;

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Override
    @Transactional
    public void saveBooking(BookingDto bookingDto) {
        Booking validateBooking = validateBooking(roomMapper.toEntity(roomService.getRoomById(bookingDto.getRoom().getId())),
                userMapper.toEntity(userService.getUserById(bookingDto.getUser().getId())), bookingMapper.toEntity(bookingDto));
        bookingRepository.save(validateBooking);
        log.info("In saveBooking - booking: {} successfully registered", bookingDto);
        sendBookingReceiptDto(validateBooking, kafkaTemplate);
    }

    @Override
    @Transactional
    public void saveBooking(Long userId, BookingDto bookingDto) {
        bookingDto.setUser(userService.getUserById(userId));
        Booking validateBooking = validateBooking(roomMapper.toEntity(roomService.getRoomById(bookingDto.getRoom().getId())),
                userMapper.toEntity(userService.getUserById(bookingDto.getUser().getId())), bookingMapper.toEntity(bookingDto));
        bookingRepository.save(validateBooking);
        log.info("In saveBooking - booking: {} successfully registered", bookingDto);
        sendBookingReceiptDto(validateBooking, kafkaTemplate);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll();
        log.info("In getAllBookings - {} booking(s) found", bookingList.size());
        return bookingList.stream()
                .filter(booking -> booking.getRoom().getDeleteTime() == null && booking.getUser().getDeleteTime() == null)
                .map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getFullBookingsList() {
        List<Booking> bookingList = bookingRepository.getFullBookingsList();
        log.info("In getFullBookingsList - {} booking(s) found", bookingList.size());
        return bookingList.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            log.warn("In getBookingById booking with id: {} not found", id);
            throw new BookingException("Booking not found");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getBookingByUserId(Long id) {
        UserDto user = userService.getUserById(id);
        List<Booking> bookingList = bookingRepository.findBookingByUserId(user.getId());
        log.info("In getBookingByUserId - {} booking(s) found", bookingList.size());
        return bookingList.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingByRoomId(Long id) {
        RoomDto room = roomService.getRoomById(id);
        List<Booking> bookingList = bookingRepository.findBookingByRoomId(room.getId());
        log.info("In getBookingByRoomId - {} booking(s) found", bookingList.size());
        return bookingList.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllBookings() {
        bookingRepository.findAll().forEach(booking -> {
            booking.setDeleteTime(new Date());
            bookingRepository.save(booking);
        });
        log.info("In deleteAllBookings all booking(s) delete");
    }

    @Override
    @Transactional
    public void deleteBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            log.warn("In deleteBookingById - booking with id: {} not found", id);
            throw new BookingException("Booking not found");
        }
        booking.setDeleteTime(new Date());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void deleteBookingById(Long id, Long userId) {
        Booking booking = bookingMapper.toEntity(getBookingByUserId(userId).stream()
                .filter(tempBooking -> tempBooking.getId().equals(id))
                .findFirst().orElse(null));
        if (booking == null) {
            log.warn("In deleteBookingById - booking with user id: {} not found", userId);
            throw new BookingException("Booking not found");
        }
        booking.setDeleteTime(new Date());
        bookingRepository.save(booking);
    }

    @Override
    public InputStreamResource downloadReceipt(Long id, String name) {
        List<Booking> bookingList = bookingRepository.findBookingByUserId(id);
        Booking currentBooking = bookingList.stream().filter(booking -> booking.getReceiptName().equals(name)).findFirst().orElse(null);
        if (currentBooking == null) {
            log.warn("In downloadReceipt - booking with user id: {} and receipt name {} not found", id, name);
            throw new BookingException("Booking not found");
        }

        File fileResponseEntity = restTemplate.getForObject(URL + name, File.class);

        InputStreamResource resource;
        try {
            if (fileResponseEntity == null) {
                log.warn("In downloadReceipt restTemplate return empty file");
                throw new RuntimeException("In downloadReceipt restTemplate return empty file");
            }
            resource = new InputStreamResource(new FileInputStream(fileResponseEntity));
            log.info("In downloadReceipt resource getting success");
        } catch (FileNotFoundException e) {
            log.warn("In downloadReceipt resource getting failed");
            throw new RuntimeException("Resource getting failed");
        }
        return resource;
    }

    private Booking validateBooking(Room room, User user, Booking booking) {
        if (room.getUnderRenovation()) {
            log.warn("In validateBooking - booking {} is under renovation", booking);
            throw new RoomException("The room is unavailable.");
        }
        int notValidBookingDates = bookingRepository.validate(booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate());
        if (notValidBookingDates == 1) {
            log.warn("In validateBooking incorrect booking dates.");
            throw new BookingException("Incorrect booking dates.");
        }
        Booking tempBooking = new Booking(room, user, booking.getStartDate(), booking.getEndDate());
        if (tempBooking.getStartDate().after(tempBooking.getEndDate())) {
            log.warn("In validateBooking start date {} is after end dates {}", tempBooking.getStartDate(), booking.getEndDate());
            throw new BookingException("Start date is after end date.");
        }
        log.info("In validateBooking booking: {} validate successful", tempBooking);
        return tempBooking;
    }

    @KafkaListener(topics = "responseReceiptCreation")
    @Transactional
    public void addReceiptName(ConsumerRecord<Long, String> record) {
        BookingReceiptInfoDto bookingReceiptInfoDto;
        try {
            bookingReceiptInfoDto = objectMapper.readValue(record.value(), BookingReceiptInfoDto.class);
            Booking booking = bookingRepository.findById(bookingReceiptInfoDto.getId()).orElse(null);
            if (booking == null) {
                log.warn("In addReceiptName bookings when id: {} not found", bookingReceiptInfoDto.getId());
                throw new BookingException("Booking not found");
            }
            booking.setReceiptName(bookingReceiptInfoDto.getUuid());
            bookingRepository.save(booking);
            log.info("In addReceiptName set booking with id: {} receipt name: {}", bookingReceiptInfoDto.getId(), bookingReceiptInfoDto.getUuid());
        } catch (JsonProcessingException e) {
            log.warn("In addReceiptName parsing string to BookingReceiptInfoDto is failed");
            throw new BookingException("Parsing kafka string to booking is failed");
        }
    }

    private static void sendBookingReceiptDto(Booking validateBooking, KafkaTemplate<Long, BookingReceiptDto> kafkaTemplate) {
        BookingReceiptDto bookingReceiptDto = new BookingReceiptDto(validateBooking.getId(), validateBooking.getRoom().getId(),
                validateBooking.getUser().getName(), validateBooking.getStartDate(), validateBooking.getEndDate());
        ListenableFuture<SendResult<Long, BookingReceiptDto>> future =
                kafkaTemplate.send("requestReceiptCreation", validateBooking.getId(), bookingReceiptDto);
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();
        log.info("In saveBooking BookingReceiptDto: {} was send", bookingReceiptDto);
    }
}
