package com.hotel.configuration.security;

import com.hotel.dto.UserDto;
import com.hotel.exception.UserException;
import com.hotel.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserDto myUser = userService.loadUserByLogin(login);
        if(myUser == null) {
            log.warn("In loadUserByUsername - user with login {} not found", login);
            throw new UserException("User with login: " + login + " not found");
        }
        log.info("In loadUserByUsername - user with login {} successfully loaded", login);
        return JwtUserFactory.create(myUser);
    }
}
