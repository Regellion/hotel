package com.hotel.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
@Where(clause = "delete_time IS NULL")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "delete_time")
    private Date deleteTime;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    public Booking(Room room, User user, Date startDate, Date endDate) {
        this.room = room;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", roomId=" + room.getId() +
                ", userId=" + user.getId() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", deleteTime=" + (deleteTime == null ? "N/A" : deleteTime) +
                '}';
    }
}
