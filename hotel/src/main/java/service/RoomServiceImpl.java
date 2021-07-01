package service;

import dto.RoomDto;
import model.Room;
import repository.RoomRepository;
import repository.RoomRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl() {
        roomRepository = new RoomRepositoryImpl();
    }

    @Override
    public Room createRoom(RoomDto roomDto) {
        return Optional.ofNullable(roomRepository.saveRoom(new Room(roomDto.getUnderRenovation(), roomDto.getPrice())))
                .orElseThrow(() -> new RuntimeException("Failed room creation"));
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> roomList = roomRepository.getAllRooms();
        if (roomList.size() == 0) {
            throw new RuntimeException("Room list is empty");
        }
        return roomList;
    }

    @Override
    public Room getRoomById(Long id) {
        return Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public void updateRoomById(Long id, RoomDto roomDto) {
        Room tempRoom = Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RuntimeException("Room not found"));
        tempRoom.setUnderRenovation(roomDto.getUnderRenovation());
        tempRoom.setPrice(roomDto.getPrice());
        roomRepository.updateRoomById(tempRoom);
    }

    @Override
    public void deleteAllRooms() {
        List<Room> roomList = roomRepository.getAllRooms();
        if (roomList.size() == 0) {
            throw new RuntimeException("Rooms list is empty");
        }
        roomRepository.deleteAllRooms();
    }

    @Override
    public void deleteRoomById(Long id) {
        Room tempRoom = Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RuntimeException("User not found"));
        roomRepository.deleteRoomById(tempRoom.getId());
    }
}
