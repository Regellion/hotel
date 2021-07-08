package com.hotel.service;

import com.hotel.exception.RoomException;
import com.hotel.dto.RoomDto;
import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(RoomDto roomDto) {
        return Optional.ofNullable(roomRepository.saveRoom(new Room(roomDto.getUnderRenovation(), roomDto.getPrice())))
                .orElseThrow(() -> new RoomException("Failed room creation"));
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> roomList = roomRepository.getAllRooms();
        if (roomList.size() == 0) {
            throw new RoomException("Room list is empty");
        }
        return roomList;
    }

    @Override
    public Room getRoomById(Long id) {
        return Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found"));
    }

    @Override
    public void updateRoomById(Long id, RoomDto roomDto) {
        Room tempRoom = Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found"));
        tempRoom.setUnderRenovation(roomDto.getUnderRenovation());
        tempRoom.setPrice(roomDto.getPrice());
        roomRepository.updateRoomById(tempRoom);
    }

    @Override
    public void deleteAllRooms() {
        List<Room> roomList = roomRepository.getAllRooms();
        if (roomList.size() == 0) {
            throw new RoomException("Rooms list is empty");
        }
        roomRepository.deleteAllRooms();
    }

    @Override
    public void deleteRoomById(Long id) {
        Room tempRoom = Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found"));
        roomRepository.deleteRoomById(tempRoom.getId());
    }
}
