package com.hotel.controller;

import com.hotel.dto.RoomDto;
import com.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDto> showAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public RoomDto getUser(@PathVariable long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("/rooms")
    public RoomDto addRoom(@RequestBody RoomDto room) {
        roomService.createRoom(new RoomDto(room.getUnderRenovation(), room.getPrice()));
        return room;
    }

    @PutMapping("/rooms/{id}")
    public RoomDto updateRoom(@PathVariable long id, @RequestBody RoomDto room) {
        roomService.updateRoomById(id, new RoomDto(room.getUnderRenovation(), room.getPrice()));
        return room;
    }

    @DeleteMapping("/rooms/{id}")
    public String deleteById(@PathVariable long id) {
        roomService.deleteRoomById(id);
        return "Room with ID = " + id + " was deleted";
    }

    @DeleteMapping("/rooms")
    public String deleteAllRooms() {
        roomService.deleteAllRooms();
        return "All rooms was deleted";
    }
}
