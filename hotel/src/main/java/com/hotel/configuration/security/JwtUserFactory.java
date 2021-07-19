package com.hotel.configuration.security;

import com.hotel.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public final class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(UserDto user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getPassword(),
                stringToGrantedAuthority(user.getRole()),
                user.getDeleteTime()
        );
    }

    private static List<GrantedAuthority> stringToGrantedAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
