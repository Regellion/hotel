package com.hotel.service;

import com.hotel.configuration.security.JwtTokenProvider;
import com.hotel.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider provider;

    @Override
    public String login(UserDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return provider.createToken(userDto.getLogin(), userDto.getRole());
    }
}
