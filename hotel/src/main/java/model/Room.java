package model;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rooms")
@Where(clause = "delete_time IS NULL")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_status")
    private Boolean isUnderRenovation;

    @Column(name = "room_price")
    private Integer price;

    @Column(name = "delete_time")
    private Date deleteTime;

    public Room() {
    }

    public Room(Boolean isUnderRenovation, Integer price) {
        this.isUnderRenovation = isUnderRenovation;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Boolean isUnderRenovation() {
        return isUnderRenovation;
    }

    public void setUnderRenovation(Boolean underRenovation) {
        isUnderRenovation = underRenovation;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", isUnderRenovation=" + isUnderRenovation +
                ", price=" + price +
                ", deleteTime=" + (deleteTime == null ? "N/A" : deleteTime) +
                '}';
    }
}
