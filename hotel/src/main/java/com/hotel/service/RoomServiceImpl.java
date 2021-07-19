package com.hotel.service;

import com.hotel.exception.RoomException;
import com.hotel.dto.RoomDto;
import com.hotel.mapper.RoomMapper;
import com.hotel.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        return roomMapper.toDto(Optional.ofNullable(roomRepository.saveRoom(roomMapper.toEntity(roomDto)))
                .orElseThrow(() -> new RoomException("Failed room creation")));
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<RoomDto> roomList = roomRepository.getAllRooms().stream().map(roomMapper::toDto).collect(Collectors.toList());
        if (roomList.size() == 0) {
            throw new RoomException("Room list is empty");
        }
        return roomList;
    }

    @Override
    public RoomDto getRoomById(Long id) {
        return roomMapper.toDto(Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found")));
    }

    @Override
    public void updateRoomById(Long id, RoomDto roomDto) {
        roomDto.setId(Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found")).getId());
        roomRepository.updateRoomById(roomMapper.toEntity(roomDto));
    }

    @Override
    public void deleteAllRooms() {
        List<RoomDto> roomList = roomRepository.getAllRooms().stream().map(roomMapper::toDto).collect(Collectors.toList());
        if (roomList.size() == 0) {
            throw new RoomException("Rooms list is empty");
        }
        roomRepository.deleteAllRooms();
    }

    @Override
    public void deleteRoomById(Long id) {
        RoomDto tempRoom = roomMapper.toDto(Optional.ofNullable(roomRepository.getRoomById(id)).orElseThrow(() -> new RoomException("Room not found")));
        roomRepository.deleteRoomById(tempRoom.getId());
    }
}
