package com.hotel.repository;

import com.hotel.model.User;

import java.util.List;

public interface UserRepository {

    User saveUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void deleteUserById(Long id);

    void deleteAllUsers();

    User updateUserById(User user);

    User findByLogin(String login);
}
