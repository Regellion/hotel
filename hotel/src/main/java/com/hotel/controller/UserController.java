package com.hotel.controller;

import com.hotel.configuration.security.JwtUser;
import com.hotel.dto.UserDto;
import com.hotel.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/admin/users/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public UserDto getUser() {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        System.out.println(currentUserId);
        return getUser(currentUserId);
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody UserDto user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping("/admin/users/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody UserDto user) {
        userService.updateUserById(id, user);
        return user;
    }

    @PutMapping("/users")
    public UserDto updateUser(@RequestBody UserDto user) {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return updateUser(currentUserId, user);
    }


    @DeleteMapping("/admin/users/{id}")
    public String deleteById(@PathVariable long id) {
        userService.deleteUserById(id);
        return "User with ID = " + id + " was deleted";
    }

    @DeleteMapping("/users")
    public String deleteById() {
        long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return deleteById(currentUserId);
    }

    @DeleteMapping("/admin/users")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "All users was deleted";
    }
}
