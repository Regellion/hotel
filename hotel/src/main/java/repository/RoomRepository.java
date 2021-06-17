package repository;

import model.Room;

import java.util.List;

public interface RoomRepository {

    Room saveRoom(Room room);

    Room getRoomById(Integer id);

    List<Room> getAllRooms();

    void deleteRoomById(Integer id);

    void deleteAllRooms();
}
