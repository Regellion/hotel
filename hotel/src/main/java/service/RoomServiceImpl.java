package service;

import model.Room;
import repository.RoomRepository;

import java.util.List;

public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        if (roomRepository.getRoomById(room.getId()) != null) {
            System.out.println("Комната под номером " + room.getId() + " уже существует!");
            return null;
        }
        return roomRepository.saveRoom(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    @Override
    public Room getRoomById(Integer id) {
        Room tempRoom = roomRepository.getRoomById(id);
        if (tempRoom == null) {
            System.out.println("Комната под номером " + id + " не существует!");
        }
        return tempRoom;
    }

    @Override
    public void updateRoomById(Room room) {
        Room tempRoom = roomRepository.getRoomById(room.getId());
        if (tempRoom == null) {
            System.out.println("Комната под номером " + room.getId() + " не существует!");
            return;
        }
        roomRepository.deleteRoomById(room.getId());
        roomRepository.saveRoom(room);
    }

    @Override
    public void deleteAllRooms() {
        roomRepository.deleteAllRooms();
    }

    @Override
    public void deleteRoomById(Integer id) {
        if (roomRepository.getRoomById(id) == null) {
            System.out.println("Комната под номером " + id + " не существует!");
            return;
        }
        roomRepository.deleteRoomById(id);
    }
}
