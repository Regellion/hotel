package com.hotel.dto;

import com.hotel.model.Booking;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private Long roomId;
    private Long userId;
    private Date startDate;
    private Date endDate;
    private String receiptName;

    public static BookingDto createBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .endDate(booking.getEndDate())
                .startDate(booking.getStartDate())
                .roomId(booking.getRoom().getId())
                .userId(booking.getUser().getId())
                .receiptName(booking.getReceiptName())
                .build();
    }
}
