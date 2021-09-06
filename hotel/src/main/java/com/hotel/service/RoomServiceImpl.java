package com.hotel.service;

import com.hotel.dto.RoomDto;
import com.hotel.exception.RoomException;
import com.hotel.mapper.RoomMapper;
import com.hotel.model.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void createRoom(RoomDto roomDto) {
        roomRepository.save(roomMapper.toEntity(roomDto));
        log.info("In createRoom - room: {} successfully registered", roomDto);
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> roomList = roomRepository.findAll();
        log.info("In getAllRooms - {} room(s) found", roomList.size());
        return roomList.stream().map(roomMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            log.warn("In getRoomById - room by room id {} is not found", id);
            throw new RoomException("Room not found");
        }
        log.info("In getRoomById - room: {} found by room id {}", room, id);
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    public void deleteAllRooms() {
        roomRepository.findAll().forEach(room -> {
            room.setDeleteTime(new Date());
            roomRepository.save(room);
        });
        bookingRepository.findAll().forEach(booking -> {
            booking.setDeleteTime(new Date());
            bookingRepository.save(booking);
        });
        log.info("In deleteAllRooms all rooms delete");
    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            log.warn("In deleteRoomById room with id: {} not found", id);
            throw new RoomException("Room not found");
        }
        room.setDeleteTime(new Date());
        bookingRepository.findBookingByRoomId(id).forEach(booking -> {
            booking.setDeleteTime(new Date());
            bookingRepository.save(booking);
        });
        roomRepository.save(room);
        log.info("In deleteRoomById - room with id: {} successful delete", id);
    }
}
