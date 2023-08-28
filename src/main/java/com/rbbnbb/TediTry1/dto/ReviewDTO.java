package com.rbbnbb.TediTry1.dto;

public class ReviewDTO {
    private Long rentalId;
    private String text;
    private Integer stars;

    public ReviewDTO(Long rentalId, String text, Integer stars) {
        this.rentalId = rentalId;
        this.text = text;
        this.stars = stars;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String toString() {
        return "Review: " + this.text + " Stars: " + this.stars;
    }
}
