package com.hotel.service;

import com.hotel.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createRoom(RoomDto roomDto);

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    void updateRoomById(Long id, RoomDto roomDto);

    void deleteAllRooms();

    void deleteRoomById(Long id);
}
