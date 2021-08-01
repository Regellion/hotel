package com.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rooms")
@Where(clause = "delete_time IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_status")
    private Boolean underRenovation;

    @Column(name = "room_price")
    private Integer price;

    @Column(name = "delete_time")
    private Date deleteTime;

    public Room(Boolean underRenovation, Integer price) {
        this.underRenovation = underRenovation;
        this.price = price;
    }
}
