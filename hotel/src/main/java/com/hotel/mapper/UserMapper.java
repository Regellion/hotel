package com.hotel.mapper;

import com.hotel.dto.UserDto;
import com.hotel.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toEntity(UserDto userDto) {
        return Objects.isNull(userDto) ? null : modelMapper.map(userDto, User.class);
    }

    public UserDto toDto(User user) {
        return Objects.isNull(user) ? null : modelMapper.map(user, UserDto.class);
    }
}
