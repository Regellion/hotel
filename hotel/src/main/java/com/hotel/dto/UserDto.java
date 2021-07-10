package com.hotel.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;

    public UserDto(String name) {
        this.name = name;
    }
}
