package com.hotel.service;

import com.hotel.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void updateUserById(Long id, UserDto userDto);

    void deleteAllUsers();

    void deleteUserById(Long id);
}
