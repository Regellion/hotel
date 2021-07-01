package dto;

public class RoomDto {
    private Long id;
    private Boolean isUnderRenovation;
    private Integer price;

    public RoomDto() {
    }

    public RoomDto(Boolean isUnderRenovation, Integer price) {
        this.isUnderRenovation = isUnderRenovation;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUnderRenovation() {
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
}
