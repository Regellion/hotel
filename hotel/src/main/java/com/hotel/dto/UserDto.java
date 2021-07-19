package com.hotel.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String role;
    private Date deleteTime;

    public UserDto(String name) {
        this.name = name;
    }
}
