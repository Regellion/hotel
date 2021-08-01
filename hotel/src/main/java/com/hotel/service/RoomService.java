package com.hotel.service;

import com.hotel.dto.RoomDto;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    void deleteAllRooms();

    void deleteRoomById(Long id);
}
