package service;

import dto.RoomDto;
import model.Room;

import java.util.List;

public interface RoomService {

    Room createRoom(RoomDto roomDto);

    List<Room> getAllRooms();

    Room getRoomById(Long id);

    void updateRoomById(Long id, RoomDto roomDto);

    void deleteAllRooms();

    void deleteRoomById(Long id);
}
