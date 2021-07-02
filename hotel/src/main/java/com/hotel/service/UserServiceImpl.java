package com.hotel.service;

import com.hotel.exception.UserException;
import com.hotel.dto.UserDto;
import com.hotel.model.User;
import com.hotel.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDto userDto) {
        return Optional.ofNullable(userRepository.saveUser(new User(userDto.getName())))
                .orElseThrow(() -> new UserException("Failed user creation"));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        if (userList.size() == 0) {
            throw new UserException("Users list is empty");
        }
        return userList;
    }

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public void updateUserById(Long id, UserDto userDto) {
        User tempUser = Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found"));
        tempUser.setName(userDto.getName());
        userRepository.updateUserById(tempUser);
    }

    @Override
    public void deleteAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        if (userList.size() == 0) {
            throw new UserException("Users list is empty");
        }
        userRepository.deleteAllUsers();
    }

    @Override
    public void deleteUserById(Long id) {
        User tempUser = Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found"));
        userRepository.deleteUserById(tempUser.getId());
    }
}
