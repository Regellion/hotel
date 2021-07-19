package com.hotel.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private RoomDto room;
    private UserDto user;
    private Date startDate;
    private Date endDate;

    public BookingDto(RoomDto room, UserDto user, Date startDate, Date endDate) {
        this.room = room;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
