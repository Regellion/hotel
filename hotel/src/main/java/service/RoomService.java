package service;

import model.Room;

import java.util.List;

public interface RoomService {
    // create
    Room createRoom(Room room);

    // read
    List<Room> getAllRooms();

    Room getRoomById(Integer id);

    //update
    void updateRoomById(Room room);

    //delete
    void deleteAllRooms();

    void deleteRoomById(Integer id);
}
