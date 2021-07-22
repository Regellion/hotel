package com.hotel.mapper;

import com.hotel.dto.RoomDto;
import com.hotel.model.Room;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class RoomMapper {
    private final ModelMapper modelMapper;

    public Room toEntity(RoomDto roomDto) {
        return Objects.isNull(roomDto) ? null : modelMapper.map(roomDto, Room.class);
    }

    public RoomDto toDto(Room room) {
        return Objects.isNull(room) ? null : modelMapper.map(room, RoomDto.class);
    }
}
