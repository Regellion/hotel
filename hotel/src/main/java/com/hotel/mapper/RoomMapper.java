package com.hotel.mapper;

import com.hotel.dto.RoomDto;
import com.hotel.model.Room;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RoomMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Room toEntity(RoomDto roomDto) {
        return Objects.isNull(roomDto) ? null : modelMapper.map(roomDto, Room.class);
    }

    public RoomDto toDto(Room room) {
        return Objects.isNull(room) ? null : modelMapper.map(room, RoomDto.class);
    }
}
