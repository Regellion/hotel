package com.hotel.dto;

import lombok.*;
import com.hotel.model.User;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String role;
    private Date deleteTime;

    public static UserDto createUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
