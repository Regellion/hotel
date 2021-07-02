package com.hotel.repository;

import com.hotel.model.Room;

import java.util.List;

public interface RoomRepository {

    Room saveRoom(Room room);

    Room getRoomById(Long id);

    List<Room> getAllRooms();

    void deleteRoomById(Long id);

    void deleteAllRooms();

    Room updateRoomById(Room room);
}
