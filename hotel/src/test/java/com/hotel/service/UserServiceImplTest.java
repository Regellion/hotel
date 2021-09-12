package com.hotel.service;

import com.hotel.dto.UserDto;
import com.hotel.exception.UserException;
import com.hotel.mapper.UserMapper;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.UserRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    ModelMapper modelMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    UserMapper userMapper;
    UserServiceImpl userService;
    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Rule
    ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        userMapper = new UserMapper(modelMapper);
        userService = new UserServiceImpl(userRepository, bookingRepository, userMapper, passwordEncoder);
    }

    @Test
    void createUser() {
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();
        userService.createUser(userMapper.toDto(user));

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        User user1 = new User();
        User user2 = new User();
        userList.add(user);
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> userDtoList = userService.getAllUsers();
        assertEquals(3, userDtoList.size());
        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(1L);

        assertEquals(userDto, UserDto.createUserDto(user));
        verify(userRepository, times(1)).findById(1L);

        Throwable thrown = assertThrows(UserException.class, () -> userService.getUserById(2L));

        assertNotNull(thrown.getMessage());
        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void deleteAllUsers() {
        userService.deleteAllUsers();
        verify(userRepository, times(1)).findAll();
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void deleteUserById() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);
        verify(bookingRepository, times(1)).findBookingByUserId(user.getId());
        verify(userRepository, times(1)).save(user);

        Throwable thrown = assertThrows(UserException.class, () -> userService.getUserById(2L));

        assertNotNull(thrown.getMessage());
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void loadUserByLogin() {
        User user = User.builder().id(1L).name("testName").login("testLogin").password("testPass").role("ADMIN").build();

        when(userRepository.findUserByLogin(user.getLogin())).thenReturn(Optional.of(user));

        UserDto userDto = userService.loadUserByLogin(user.getLogin());

        assertEquals(userDto, userMapper.toDto(user));
        verify(userRepository, times(1)).findUserByLogin(user.getLogin());


        String invalidLogin = "Some invalid login";
        Throwable thrown = assertThrows(UserException.class, () -> userService.loadUserByLogin(invalidLogin));

        assertNotNull(thrown.getMessage());
        assertEquals("User with login: " + invalidLogin + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findUserByLogin(invalidLogin);
    }
}