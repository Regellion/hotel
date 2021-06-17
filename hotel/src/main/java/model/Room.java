package model;

public class Room {
    private Integer id;
    private Boolean isBlocked;
    private Boolean isUnderRenovation;
    private Integer price;

    public Room() {
    }

    public Room(Integer id, Boolean isBlocked, Boolean isUnderRenovation, Integer price) {
        this.id = id;
        this.isBlocked = isBlocked;
        this.isUnderRenovation = isUnderRenovation;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
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

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", isBlocked=" + isBlocked +
                ", isUnderRenovation=" + isUnderRenovation +
                ", price=" + price +
                '}';
    }
}
