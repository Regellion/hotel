package com.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingReceiptDto {
    private Long id;
    private Long roomId;
    private String userName;
    private Date startDate;
    private Date endDate;
}
