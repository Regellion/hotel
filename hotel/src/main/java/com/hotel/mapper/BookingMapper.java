package com.hotel.mapper;

import com.hotel.dto.BookingDto;
import com.hotel.model.Booking;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final ModelMapper modelMapper;

    public Booking toEntity(BookingDto bookingDto) {
        return Objects.isNull(bookingDto) ? null : modelMapper.map(bookingDto, Booking.class);
    }

    public BookingDto toDto(Booking booking) {
        return Objects.isNull(booking) ? null : modelMapper.map(booking, BookingDto.class);
    }
}
