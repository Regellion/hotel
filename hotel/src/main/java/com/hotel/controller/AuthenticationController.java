package com.hotel.controller;

import com.hotel.configuration.security.JwtTokenProvider;
import com.hotel.dto.UserDto;
import com.hotel.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider provider;
    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody UserDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
            UserDto user = userService.loadUserByLogin(userDto.getLogin());
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + userDto.getLogin() + " not found");
            }
            String token = provider.createToken(user.getLogin(), user.getRole());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", userDto.getLogin());
            response.put("token", token);
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
