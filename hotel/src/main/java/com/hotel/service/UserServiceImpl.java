package com.hotel.service;

import com.hotel.exception.UserException;
import com.hotel.dto.UserDto;
import com.hotel.mapper.UserMapper;
import com.hotel.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.toDto(Optional.ofNullable(userRepository.saveUser(userMapper.toEntity(userDto)))
                .orElseThrow(() -> new UserException("Failed user creation")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userList = userRepository.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
        if (userList.size() == 0) {
            throw new UserException("Users list is empty");
        }
        return userList;
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found")));
    }

    @Override
    public void updateUserById(Long id, UserDto userDto) {
        userDto.setId(Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found")).getId());
        userRepository.updateUserById(userMapper.toEntity(userDto));
    }

    @Override
    public void deleteAllUsers() {
        List<UserDto> userList = userRepository.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
        if (userList.size() == 0) {
            throw new UserException("Users list is empty");
        }
        userRepository.deleteAllUsers();
    }

    @Override
    public void deleteUserById(Long id) {
        UserDto tempUser = userMapper.toDto(Optional.ofNullable(userRepository.getUserById(id)).orElseThrow(() -> new UserException("User not found")));
        userRepository.deleteUserById(tempUser.getId());
    }
}
