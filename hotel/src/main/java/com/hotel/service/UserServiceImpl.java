package com.hotel.service;

import com.hotel.exception.UserException;
import com.hotel.dto.UserDto;
import com.hotel.mapper.UserMapper;
import com.hotel.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.toDto(Optional.ofNullable(userRepository.saveUser(userMapper.toEntity(userDto)))
                .orElseThrow(() -> new UserException("Failed user creation")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new UserException("User not found")));
    }

    @Override
    public void updateUserById(Long id, UserDto userDto) {
        userDto.setId(Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new UserException("User not found")).getId());
        userRepository.updateUserById(userMapper.toEntity(userDto));
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAllUsers();
    }

    @Override
    public void deleteUserById(Long id) {
        UserDto tempUser = userMapper.toDto(Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new UserException("User not found")));
        userRepository.deleteUserById(tempUser.getId());
    }

    public UserDto loadUserByUsername(String login) {
        return userMapper.toDto(Optional.ofNullable(userRepository.findByLogin(login))
                .orElseThrow(() -> new UserException("User with login: " + login + " not found")));
    }

    public long getIdByUsername(String username) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.toDto(userRepository.findByLogin(userDetails.getUsername())).getId();
    }
}
