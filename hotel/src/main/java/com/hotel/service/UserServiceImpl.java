package com.hotel.service;

import com.hotel.dto.UserDto;
import com.hotel.exception.UserException;
import com.hotel.mapper.UserMapper;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.UserRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("In createUser - user: {} successfully registered", user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("In getAllUsers - {} user(s) found", userList.size());
        return userList.stream().map(UserDto::createUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("In getUserById user with id: {} not found", id);
            throw new UserException("User not found");
        }
        log.info("In getUserById - user: {} found by user id {}", user, id);
        return UserDto.createUserDto(user);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        userRepository.findAll().forEach(user -> {
            user.setDeleteTime(new Date());
            userRepository.save(user);
        });
        bookingRepository.findAll().forEach(booking -> {
            booking.setDeleteTime(new Date());
            bookingRepository.save(booking);
        });
        log.info("In deleteAllUsers all users delete");
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("In deleteUserById user with id: {} not found", id);
            throw new UserException("User not found");
        }
        user.setDeleteTime(new Date());
        bookingRepository.findBookingByUserId(id).forEach(booking -> {
            booking.setDeleteTime(new Date());
            bookingRepository.save(booking);
        });
        userRepository.save(user);
        log.info("In deleteUserById - user with id: {} successful delete", id);
    }

    @Override
    public UserDto loadUserByLogin(String login) {
        User user = userRepository.findUserByLogin(login).orElse(null);
        if (user == null) {
            log.warn("In loadUserByLogin user by login: {} not found", login);
            throw new UserException("User with login: " + login + " not found");
        }
        log.info("In loadUserByLogin - user: {} found by login: {}", user, login);
        return userMapper.toDto(user);
    }
}
