package com.hotel.controller;

import com.hotel.dto.UserDto;
import com.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody UserDto user) {
        userService.createUser(new UserDto(user.getName()));
        return user;
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserDto user) {
        userService.updateUserById(id, new UserDto(user.getName()));
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteById(@PathVariable long id) {
        userService.deleteUserById(id);
        return "User with ID = " + id + " was deleted";
    }

    @DeleteMapping("/users")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "All users was deleted";
    }
}
