package com.hotel.controller;

import com.hotel.dto.RoomDto;
import com.hotel.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDto> showAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public RoomDto getRoom(@PathVariable long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("/admin/rooms")
    public RoomDto addRoom(@RequestBody RoomDto room) {
        roomService.createRoom(room);
        return room;
    }

    @PutMapping("/admin/rooms")
    public RoomDto updateRoom(@RequestBody RoomDto room) {
        roomService.createRoom(room);
        return room;
    }

    @DeleteMapping("/admin/rooms/{id}")
    public String deleteById(@PathVariable long id) {
        roomService.deleteRoomById(id);
        return "Room with ID = " + id + " was deleted";
    }

    @DeleteMapping("/admin/rooms")
    public String deleteAllRooms() {
        roomService.deleteAllRooms();
        return "All rooms was deleted";
    }
}
