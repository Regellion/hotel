package com.hotel.controller;

import com.hotel.dto.UserDto;
import com.hotel.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final LoginService loginService;

    @PostMapping("/api/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody UserDto userDto) {
        String token = loginService.login(userDto);
        Map<Object, Object> response = new HashMap<>();
        response.put("username", userDto.getLogin());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

}
