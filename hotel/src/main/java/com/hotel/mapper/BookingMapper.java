package com.hotel.mapper;

import com.hotel.dto.BookingDto;
import com.hotel.dto.RoomDto;
import com.hotel.model.Booking;
import com.hotel.model.Room;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookingMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Booking toEntity(BookingDto bookingDto) {
        return Objects.isNull(bookingDto) ? null : modelMapper.map(bookingDto, Booking.class);
    }

    public BookingDto toDto(Booking booking) {
        return Objects.isNull(booking) ? null : modelMapper.map(booking, BookingDto.class);
    }
}
