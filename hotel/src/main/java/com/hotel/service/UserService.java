package com.hotel.service;

import com.hotel.dto.UserDto;

import java.util.List;

public interface UserService {

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteAllUsers();

    void deleteUserById(Long id);

    UserDto loadUserByLogin(String login);
}
