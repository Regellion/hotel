package com.hotel.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private Boolean underRenovation;
    private Integer price;

    public RoomDto(Boolean underRenovation, Integer price) {
        this.underRenovation = underRenovation;
        this.price = price;
    }
}
