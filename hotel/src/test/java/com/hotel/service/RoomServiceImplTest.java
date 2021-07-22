package com.hotel.service;

import com.hotel.dto.RoomDto;
import com.hotel.exception.RoomException;
import com.hotel.mapper.RoomMapper;
import com.hotel.model.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    ModelMapper modelMapper;

    @Mock
    RoomRepository roomRepository;
    @Mock
    BookingRepository bookingRepository;

    RoomMapper roomMapper;

    RoomServiceImpl roomService;

    @Rule
    ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        roomMapper = new RoomMapper(modelMapper);
        roomService = new RoomServiceImpl(roomRepository, roomMapper, bookingRepository);
    }

    @Test
    void createRoom() {
        Room room = Room.builder().underRenovation(true).price(500).build();
        roomService.createRoom(roomMapper.toDto(room));

        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        Room room = Room.builder().underRenovation(false).price(1000).build();
        Room room1 = Room.builder().underRenovation(false).price(2000).build();
        Room room2 = Room.builder().underRenovation(true).price(3000).build();
        roomList.add(room);
        roomList.add(room1);
        roomList.add(room2);

        when(roomRepository.findAll()).thenReturn(roomList);
        List<RoomDto> newRoomDtoList = roomService.getAllRooms();
        assertEquals(3, newRoomDtoList.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomById() {
        Room room = Room.builder().id(1L).underRenovation(true).price(500).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomDto gettingRoomDto = roomService.getRoomById(1L);

        assertEquals(gettingRoomDto, roomMapper.toDto(room));
        verify(roomRepository, times(1)).findById(1L);

        Throwable thrown = assertThrows(RoomException.class, () -> roomService.getRoomById(2L));

        assertNotNull(thrown.getMessage());
        assertEquals("Room not found", thrown.getMessage());
        verify(roomRepository, times(1)).findById(2L);
    }

    @Test
    void deleteAllRooms() {
        roomService.deleteAllRooms();
        verify(roomRepository, times(1)).findAll();
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void deleteRoomById() {
        Room room = Room.builder().id(1L).underRenovation(true).price(500).build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.deleteRoomById(1L);
        verify(bookingRepository, times(1)).findBookingByRoomId(room.getId());
        verify(roomRepository, times(1)).save(room);

        Throwable thrown = assertThrows(RoomException.class, () -> roomService.getRoomById(2L));

        assertNotNull(thrown.getMessage());
        assertEquals("Room not found", thrown.getMessage());
    }
}