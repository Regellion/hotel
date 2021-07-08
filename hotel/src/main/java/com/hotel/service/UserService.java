package com.hotel.service;

import com.hotel.dto.UserDto;
import com.hotel.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);

    List<User> getAllUsers();

    User getUserById(Long id);

    void updateUserById(Long id, UserDto userDto);

    void deleteAllUsers();

    void deleteUserById(Long id);
}
