package com.hotel.configuration.security;

import com.hotel.dto.UserDto;
import com.hotel.exception.UserException;
import com.hotel.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserDto myUser = Optional.ofNullable(userService.loadUserByUsername(login))
                .orElseThrow(() -> new UserException("User with login: " + login + " not found"));
        return JwtUserFactory.create(myUser);
    }
}
