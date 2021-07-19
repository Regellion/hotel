package com.hotel.mapper;

import com.hotel.dto.UserDto;
import com.hotel.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;

    public User toEntity(UserDto userDto) {
        return Objects.isNull(userDto) ? null : modelMapper.map(userDto, User.class);
    }

    public UserDto toDto(User user) {
        return Objects.isNull(user) ? null : modelMapper.map(user, UserDto.class);
    }
}